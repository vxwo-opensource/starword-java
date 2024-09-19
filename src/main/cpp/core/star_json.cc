#include "star_json.h"

#include "jwbase.h"

const JWChar kSTAR = '*';
const JWChar kBLANK = ' ';
const JWChar kCOLON = ':';
const JWChar kCOMMA = ',';
const JWChar kBACKSLASH = '\\';
const JWChar kDOUBLE_QUOTE = '\"';
const JWChar kLEFT_BRACE = '{';
const JWChar kLEFT_BRACKET = '[';

static size_t BufferSkipChar(const JWCharBuffer buffer, JWChar target,
                             size_t begin_index, size_t end_index) {
  size_t index = begin_index;
  while (index < end_index && buffer[index] == target) {
    ++index;
  }
  return index - begin_index;
}

static size_t BufferForwardSkipChar(const JWCharBuffer buffer, JWChar target,
                                    size_t begin_index, size_t end_index) {
  size_t index = end_index;
  while (index >= begin_index && buffer[index] == target) {
    --index;
  }
  return end_index - index;
}

static ssize_t BufferStrIndexOf(const JWCharBuffer buffer, size_t begin_index,
                                size_t end_index, const JWCharBuffer str,
                                size_t str_begin_index, size_t str_end_index) {
  JWChar first_char = str[str_begin_index];
  size_t str_count = str_end_index - str_begin_index;

  size_t stop_index = end_index - str_count;
  for (size_t i = begin_index; i <= stop_index; i++) {
    if (buffer[i] != first_char) {
      while (++i <= stop_index && buffer[i] != first_char);
    }

    if (i <= stop_index) {
      size_t j = i + 1;
      size_t end = j + str_count - 1;
      for (size_t k = str_begin_index + 1; j < end && buffer[j] == str[k];
           j++, k++);
      if (j == end) {
        return i;
      }
    }
  }

  return -1;
}

static ssize_t jwstr_last_index_of(const JWCharBuffer buffer,
                                   size_t begin_index, size_t end_index,
                                   JWChar ch) {
  for (size_t i = end_index - 1; i >= begin_index; i--) {
    if (buffer[i] == ch) {
      return i;
    }
  }

  return -1;
}

StarJson::StarJson(bool skip_number, size_t border)
    : skip_number_(skip_number), border_(border) {}

StarJson::~StarJson() {}

void StarJson::AddPrefix(const JWCharBuffer buffer, size_t length) {
  tree_.AddWord(kDOUBLE_QUOTE, buffer, length, 0);
}

bool StarJson::ProcessBuffer(JWCharBuffer buffer, size_t length) {
  StarContext context{0, 0};

  size_t index = 0;
  TrieFound found;
  while (index < length) {
    // Find the KEY prefix
    if (!tree_.SearchWord(found, buffer, index, length)) {
      break;
    }

    if (found.end_index >= length) {
      break;
    }

    // Find an exact matching for the KEY suffix
    ssize_t key_suffix_begin_index =
        FindKeyEnd(buffer, found.end_index, length);
    if (key_suffix_begin_index < 0) {
      index = found.end_index;
      continue;
    } else if (key_suffix_begin_index != found.end_index) {
      index = key_suffix_begin_index;
      continue;
    }

    // Find the symbol double quote
    ssize_t key_suffix_end_index = FindSoftCharEnd(
        buffer, kBACKSLASH, kDOUBLE_QUOTE, key_suffix_begin_index, length);
    if (key_suffix_end_index < 0) {
      index = key_suffix_begin_index;
      continue;
    }

    // Find the symbol colon
    ssize_t colon_end_index =
        FindSoftCharEnd(buffer, kBLANK, kCOLON, key_suffix_end_index, length);
    if (colon_end_index < 0) {
      index = key_suffix_end_index;
    } else {
      index =
          ProcessComplexValue(context, buffer, colon_end_index, length, true);
    }
  }

  return context.count > 0;
}

size_t StarJson::ProcessComplexValue(StarContext &context, JWCharBuffer buffer,
                                     size_t begin_index, size_t end_index,
                                     bool enter_array) {
  // Skip blank
  size_t value_prefix_begin_index =
      begin_index + BufferSkipChar(buffer, kBLANK, begin_index, end_index);
  if (value_prefix_begin_index >= end_index) {
    return value_prefix_begin_index;
  }

  // Test value is symbol
  ssize_t symbol_end_index =
      FindSymbolEnd(buffer, value_prefix_begin_index, end_index);
  if (symbol_end_index != -1) {
    return symbol_end_index;
  }

  JWChar first_char = buffer[value_prefix_begin_index];

  // Test value is object
  if (first_char == kLEFT_BRACE) {
    return value_prefix_begin_index + 1;
  }

  if (first_char != kLEFT_BRACKET) {
    return ProcessSimpleValue(context, buffer, value_prefix_begin_index,
                              end_index);
  } else if (!enter_array) {
    return value_prefix_begin_index + 1;
  } else {
    size_t index = value_prefix_begin_index + 1;
    while (index < end_index) {
      size_t value_end_index =
          ProcessComplexValue(context, buffer, index, end_index, false);

      // Skip blank
      value_end_index +=
          BufferSkipChar(buffer, kBLANK, value_end_index, end_index);
      if (value_end_index >= end_index) {
        index = value_end_index;
        break;
      }

      index = value_end_index + 1;

      // Find the symbol comma
      JWChar stop_char = buffer[value_end_index];
      if (stop_char != kCOMMA) {
        break;
      }
    }

    return index;
  }
}

size_t StarJson::ProcessSimpleValue(StarContext &context, JWCharBuffer buffer,
                                    size_t begin_index, size_t end_index) {
  // Find the number
  ssize_t number_end_index = FindNumberEnd(buffer, begin_index, end_index);
  if (number_end_index != -1) {
    StarBuffer(context, buffer, begin_index, number_end_index, true);
    return number_end_index;
  }

  // Find the symbol double quote
  ssize_t value_prefix_end_index = FindSoftCharEnd(
      buffer, kBACKSLASH, kDOUBLE_QUOTE, begin_index, end_index);
  if (value_prefix_end_index < 0) {
    return begin_index;
  }

  size_t value_prefix_length = value_prefix_end_index - begin_index;

  // Find the the VALUE suffix
  ssize_t value_suffix_begin_index = -1;
  size_t index = value_prefix_end_index;
  while (index < end_index) {
    value_suffix_begin_index = BufferStrIndexOf(
        buffer, index, end_index, buffer, begin_index, value_prefix_end_index);
    if (value_suffix_begin_index < 0) {
      break;
    }

    // Skip escape characters for an odd number of double quotes
    ssize_t number_backslash =
        BufferForwardSkipChar(buffer, kBACKSLASH, value_prefix_end_index,
                              value_suffix_begin_index - 1);
    if (number_backslash % 2 == 0) {
      break;
    }

    index = value_suffix_begin_index + value_prefix_length;
    value_suffix_begin_index = -1;
  }

  // SKip no VALUE suffix
  if (value_suffix_begin_index < 0) {
    return begin_index;
  }

  if (value_suffix_begin_index > value_prefix_end_index) {
    StarBuffer(context, buffer, value_prefix_end_index,
               value_suffix_begin_index, false);
  }

  return value_suffix_begin_index + value_prefix_length;
}

void StarJson::StarBuffer(StarContext &context, JWCharBuffer buffer,
                          size_t begin_index, size_t end_index,
                          bool is_number) {
  if (skip_number_ && is_number) {
    return;
  }

  if (begin_index > 0 && buffer[begin_index - 1] == kBACKSLASH &&
      BufferForwardSkipChar(buffer, kBACKSLASH, 0, begin_index - 1) % 2 != 0) {
    begin_index += 1;
  }

  if (end_index > 0 && buffer[end_index - 1] == kBACKSLASH &&
      BufferForwardSkipChar(buffer, kBACKSLASH, begin_index, end_index - 1) %
              2 !=
          0) {
    end_index -= 1;
  }

  ssize_t effective = end_index - begin_index;
  if (effective < 1) {
    return;
  }

  ssize_t left_border = border_;
  ssize_t right_border = border_;
  if (left_border >= effective) {
    left_border = effective - 1;
  }
  if (left_border < 0) {
    left_border = 0;
  }
  if (right_border >= effective - left_border) {
    right_border = 0;
  }
  if (left_border > 0) {
    begin_index += left_border;
  }
  if (right_border > 0) {
    end_index -= right_border;
  }

  if (begin_index < end_index) {
    context.count += 1;
    context.char_length += end_index - begin_index;

    while (begin_index < end_index) {
      buffer[begin_index++] = kSTAR;
    }
  }
}

ssize_t StarJson::FindKeyEnd(const JWCharBuffer buffer, size_t begin_index,
                             size_t end_index) {
  ssize_t key_end_index = -1;
  size_t index = begin_index;
  while (index < end_index) {
    JWChar ch = buffer[index];
    if (ch == kBACKSLASH || ch == kDOUBLE_QUOTE) {
      key_end_index = index;
      break;
    }

    ++index;
  }

  return key_end_index;
}

ssize_t StarJson::FindSoftCharEnd(const JWCharBuffer buffer, JWChar skip,
                                  JWChar target, size_t begin_index,
                                  size_t end_index) {
  size_t index =
      begin_index + BufferSkipChar(buffer, skip, begin_index, end_index);
  if (index >= end_index || buffer[index] != target) {
    return -1;
  }
  return index + 1;
}

ssize_t StarJson::FindSymbolEnd(const JWCharBuffer buffer, size_t begin_index,
                                size_t end_index) {
  size_t size = end_index - begin_index;
  if (size < 4) {
    return -1;
  }

  JWChar first_char = buffer[begin_index];

  if (first_char == 'n') {  // Test the symbol null
    if (buffer[begin_index + 1] == 'u' && buffer[begin_index + 2] == 'l' &&
        buffer[begin_index + 3] == 'l') {
      return begin_index + 4;
    }
  } else if (first_char == 't') {  // Test the symbol true
    if (buffer[begin_index + 1] == 'r' && buffer[begin_index + 2] == 'u' &&
        buffer[begin_index + 3] == 'e') {
      return begin_index + 4;
    }
  } else if (size > 4 && first_char == 'f') {  // Test the symbol false
    if (buffer[begin_index + 1] == 'a' && buffer[begin_index + 2] == 'l' &&
        buffer[begin_index + 3] == 's' && buffer[begin_index + 4] == 'e') {
      return begin_index + 5;
    }
  }

  return -1;
}

ssize_t StarJson::FindNumberEnd(const JWCharBuffer buffer, size_t begin_index,
                                size_t end_index) {
  size_t index = begin_index;
  while (index < end_index) {
    JWChar ch = buffer[index];
    if (!((ch >= '0' && ch <= '9') || ch == '.' || ch == '-' || ch == '+' ||
          ch == 'e' || ch == 'E')) {
      break;
    }

    ++index;
  }

  return index <= begin_index ? -1 : index;
}
