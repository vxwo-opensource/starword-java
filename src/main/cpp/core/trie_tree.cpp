#include "trie_tree.h"
#include "jwbase.h"
#include <stack>
#include <unordered_map>

typedef struct TrieTree::TrieNode {
  bool block;
  JWChar ch;
  int value;
  size_t length;
  std::unordered_map<JWChar, struct TrieNode *> children;
} TrieNode;

static TrieNode *link_children(TrieNode *current, JWChar ch) {
  auto child = current->children.find(ch);
  if (child != current->children.end()) {
    current = child->second;
  } else {
    TrieNode *node = new TrieNode();
    node->block = false;
    node->ch = ch;
    current->children.insert(std::make_pair(ch, node));
    current = node;
  }

  return current;
}

TrieTree::TrieTree() : root_(new TrieNode()) {}

TrieTree::~TrieTree() {
  std::stack<TrieNode *> nodes;

  for (auto it = root_->children.begin(); it != root_->children.end(); ++it) {
    nodes.push(it->second);
  }
  root_->children.clear();

  while (!nodes.empty()) {
    TrieNode *node = nodes.top();
    if (node->children.empty()) {
      nodes.pop();
      delete node;
    } else {
      for (auto it = node->children.begin(); it != node->children.end(); ++it) {
        nodes.push(it->second);
      }
      node->children.clear();
    }
  }

  delete root_;
}

bool TrieTree::empty() const { return root_->children.empty(); }

void TrieTree::append(TrieNode *node, const JWCharBuffer buffer, size_t length,
                      int value, size_t base_length) {
  TrieNode *current = node;
  for (size_t i = 0; i < length; ++i) {
    current = link_children(current, buffer[i]);
  }

  if (!current->block) {
    current->block = true;
    current->value = value;
    current->length = base_length + length;
  }
}

void TrieTree::insert(const JWCharBuffer buffer, size_t length, int value) {
  append(root_, buffer, length, value, 0);
}

void TrieTree::insert(JWChar prefix, const JWCharBuffer buffer, size_t length,
                      int value) {
  append(link_children(root_, prefix), buffer, length, value, 1);
}

TrieNode *TrieTree::find(const JWCharBuffer buffer, size_t begin_index,
                         size_t end_index) const {
  TrieNode *current = root_;
  std::vector<TrieNode *> cached;

  for (size_t i = begin_index; i < end_index; i++) {
    auto child = current->children.find(buffer[i]);
    if (child == current->children.end()) {
      break;
    }

    current = child->second;
    if (current->block) {
      cached.push_back(current);
    }
  }

  return cached.empty() ? nullptr : cached.back();
}

bool TrieTree::parse(TrieIndex &found, const JWCharBuffer buffer,
                     size_t begin_index, size_t end_index) const {
  size_t index = begin_index;
  while (index < end_index) {
    TrieNode *node = find(buffer, index, end_index);
    if (node != nullptr) {
      found.value = node->value;
      found.begin_index = index;
      found.end_index = index + node->length;
      return true;
    }

    ++index;
  }

  return false;
}
