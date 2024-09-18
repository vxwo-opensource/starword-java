#pragma once

#include "jwbase.h"

typedef struct TrieIndex {
  int value;
  size_t begin_index;
  size_t end_index;
} TrieIndex;

class TrieTree final {
  struct TrieNode;

public:
  TrieTree();
  ~TrieTree();

  bool empty() const;

  void insert(const JWCharBuffer buffer, size_t length, int value);
  void insert(JWChar prefix, const JWCharBuffer buffer, size_t length,
              int value);

  bool parse(TrieIndex &found, const JWCharBuffer buffer, size_t begin_index,
             size_t end_index) const;

private:
  TrieNode *root_;

  void append(TrieNode *node, const JWCharBuffer buffer, size_t length,
              int value, size_t base_length);

  TrieNode *find(const JWCharBuffer buffer, size_t begin_index,
                 size_t end_index) const;
};
