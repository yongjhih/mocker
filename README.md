# Mocker

[![JitPack](https://img.shields.io/github/tag/yongjhih/mocker.svg?label=JitPack)](https://jitpack.io/#yongjhih/mocker)
[![Download](https://api.bintray.com/packages/yongjhih/maven/mocker/images/download.svg) ](https://bintray.com/yongjhih/maven/mocker/_latestVersion)
[![javadoc](https://img.shields.io/github/tag/yongjhih/mocker.svg?label=javadoc)](https://jitpack.io/com/github/yongjhih/mocker/-SNAPSHOT/javadoc/)
[![Build Status](https://travis-ci.org/yongjhih/mocker.svg)](https://travis-ci.org/yongjhih/mocker)
[![Coverage Status](https://coveralls.io/repos/github/yongjhih/mocker/badge.svg?branch=master)](https://coveralls.io/github/yongjhih/mocker?branch=master)

## Usage

```java
ParseUser mockUser = Mocker.of(ParseUser.class)
    .when(user -> user.getObjectId()).thenReturn(user -> "1_" + user.hashCode());
    .mock();
```

```java
ParseQuery<ParseUser> mockQuery = Mocker.of(mock(ParseQuery.class))
    .when(query -> query.countInBackground()).thenReturn(query -> Task.forResult(users.size()))
    .when(query -> query.findInBackground()).thenReturn(query -> Task.forResult(users))
    .when(query -> query.setSkip(any(int.class))).thenReturn(query -> null)
    .when(query -> query.setLimit(any(int.class))).thenReturn(query -> null)
    .mock();
```
