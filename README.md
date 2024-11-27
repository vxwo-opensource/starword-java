starword-java
===================================

A native use `star` mark words for Java

# Native support

| *OS*    | *Arch*  | *Test Platform*                |
|---------|---------|--------------------------------|
| windows | x86_64  | windows 11 + openjk8,17        |
| linux   | x86_64  | docker + maven:3.8.1-openjdk-8 |
| darwin  | x86_64  | macOS 12.7.6 + openjdk8,17     |
| darwin  | aarch64 | -                              |

# Usage

```xml
<project>

  <properties>
    <starword.version>0.9.8</starword.version>

    ... others
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.vxwo.free</groupId>
      <artifactId>starword</artifactId>
      <version>${starword.version}</version>
    </dependency>

  ... others
  </dependencies>

  ... others
</project>
```
