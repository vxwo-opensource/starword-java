#pragma once

#include "jwbase.h"
#include "trie_tree.h"

class StarJson final {
  typedef struct StarContext {
    size_t count;
    size_t char_length;
  } StarContext;

public:
  StarJson(bool skip_number, size_t border);
  ~StarJson();

  bool empty() const { return tree_.empty(); };

  void add_prefix(const JWCharBuffer buffer, size_t length);
  bool process_buffer(JWCharBuffer buffer, size_t length);

private:
  bool skip_number_;
  size_t border_;
  TrieTree tree_;

  size_t process_complex_value(StarContext &context, JWCharBuffer buffer,
                               size_t begin_index, size_t end_index,
                               bool enter_array);
  size_t process_simple_value(StarContext &context, JWCharBuffer buffer,
                              size_t begin_index, size_t end_index);

  void star_buffer(StarContext &context, JWCharBuffer buffer,
                   size_t begin_index, size_t end_index, bool is_number);

  static ssize_t find_key_end(const JWCharBuffer buffer, size_t begin_index,
                              size_t end_index);
  static ssize_t find_soft_char_end(const JWCharBuffer buffer, JWChar skip,
                                    JWChar target, size_t begin_index,
                                    size_t end_index);
  static ssize_t find_symbol_end(const JWCharBuffer buffer, size_t begin_index,
                                 size_t end_index);

  static ssize_t find_number_end(const JWCharBuffer buffer, size_t begin_index,
                                 size_t end_index);
};
