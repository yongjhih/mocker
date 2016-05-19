# Mocker

[![JitPack](https://img.shields.io/github/tag/yongjhih/mocker.svg?label=JitPack)](https://jitpack.io/#yongjhih/mocker)
[![Coverage Status](https://coveralls.io/repos/github/yongjhih/mocker/badge.svg?branch=master)](https://coveralls.io/github/yongjhih/mocker?branch=master)
<!--[![Build Status](https://travis-ci.org/yongjhih/mocker.svg)](https://travis-ci.org/yongjhih/mocker)-->
<!--[![javadoc](https://img.shields.io/github/tag/yongjhih/mocker.svg?label=javadoc)](https://jitpack.io/com/github/yongjhih/mocker/-SNAPSHOT/javadoc/)-->
<!--[![Download](https://api.bintray.com/packages/yongjhih/maven/mocker/images/download.svg) ](https://bintray.com/yongjhih/maven/mocker/_latestVersion)-->

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

Avoid unnecessary variables:

Before:

```java
ParseUser user = mock(ParseUser.class);
ParseUser user2 = mock(ParseUser.class);
ParseUser user3 = mock(ParseUser.class);
List<ParseUser> users = Arrays.asList(user, user2, user3);

ParseQuery<ParseUser> query = (ParseQuery<ParseUser>) mock(ParseQuery.class);

when(query.countInBackground()).thenReturn(Task.forResult(users.size()));
when(query.findInBackground()).thenReturn(Task.forResult(users));
when(query.setSkip(any(int.class))).thenReturn(null);
when(query.setLimit(any(int.class))).thenReturn(null);

when(user.getObjectId()).thenReturn("1_" + user.hashCode());
when(user2.getObjectId()).thenReturn("2_" + user2.hashCode());
when(user3.getObjectId()).thenReturn("3_" + user3.hashCode());

rx.assertions.RxAssertions.assertThat(rx.parse.ParseObservable.all(query)).completes();
```

After:

```java
List<ParseUser> users = Arrays.asList(
        Mocker.of(ParseUser.class).when(user -> user.getObjectId()).thenReturn(user -> "1_" + user.hashCode()).mock(),
        Mocker.of(ParseUser.class).when(user -> user.getObjectId()).thenReturn(user -> "2_" + user.hashCode()).mock(),
        Mocker.of(ParseUser.class).when(user -> user.getObjectId()).thenReturn(user -> "3_" + user.hashCode()).mock());

rx.assertions.RxAssertions.assertThat(rx.parse.ParseObservable.all(Mocker.of(ParseQuery.class)
            .when(query -> query.countInBackground()).thenReturn(query -> Task.forResult(users.size()))
            .when(query -> query.findInBackground()).thenReturn(query -> Task.forResult(users))
            .when(query -> query.setSkip(any(int.class))).thenReturn(query -> null)
            .when(query -> query.setLimit(any(int.class))).thenReturn(query -> null).mock())
        ).completes();
```
