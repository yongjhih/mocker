# mocker

[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23207-blue.svg)](http://androidweekly.net/issues/issue-207)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-mocker-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3626)
[![JitPack](https://img.shields.io/github/tag/yongjhih/mocker.svg?label=JitPack)](https://jitpack.io/#yongjhih/mocker)
[![javadoc](https://img.shields.io/github/tag/yongjhih/mocker.svg?label=javadoc)](https://jitpack.io/com/github/yongjhih/mocker/-SNAPSHOT/javadoc/)
[![Build Status](https://travis-ci.org/yongjhih/mocker.svg)](https://travis-ci.org/yongjhih/mocker)
[![Coverage Status](https://coveralls.io/repos/github/yongjhih/mocker/badge.svg)](https://coveralls.io/github/yongjhih/mocker)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c624b6f3c24d47b4917768c70e951dc2)](https://codacy.com/app/yongjhih/mocker)
<!--[![Download](https://api.bintray.com/packages/yongjhih/maven/mocker/images/download.svg) ](https://bintray.com/yongjhih/maven/mocker/_latestVersion)-->

![](art/mocker.png)

Fluent Mockito Builder

## Usage

mock():

```java
ParseUser mockUser = mocker(ParseUser.class).mock();
```

Infer casting:

```java
// Before: ParseQuery<ParseUser> mockQuery = (ParseQuery<ParseUser>) Mockito.mock(ParseQuery.class);
// After:
ParseQuery<ParseUser> mockQuery = mocker(ParseQuery.class).mock();
```

when-thenReturn:

```java
ParseUser mockUser = mocker(ParseUser.class)
    .when(user -> user.getObjectId()).thenReturn(user -> "1_" + user.hashCode());
    .mock();
```

Multiple when-thenReturn:

```java
ParseQuery<ParseUser> mockQuery = mocker(ParseQuery.class)
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
        mocker(ParseUser.class).when(user -> user.getObjectId()).thenReturn(user -> "1_" + user.hashCode()).mock(),
        mocker(ParseUser.class).when(user -> user.getObjectId()).thenReturn(user -> "2_" + user.hashCode()).mock(),
        mocker(ParseUser.class).when(user -> user.getObjectId()).thenReturn(user -> "3_" + user.hashCode()).mock());

rx.assertions.RxAssertions.assertThat(rx.parse.ParseObservable.all(mocker(ParseQuery.class)
            .when(query -> query.countInBackground()).thenReturn(query -> Task.forResult(users.size()))
            .when(query -> query.findInBackground()).thenReturn(query -> Task.forResult(users))
            .when(query -> query.setSkip(any(int.class))).thenReturn(query -> null)
            .when(query -> query.setLimit(any(int.class))).thenReturn(query -> null)
            .mock())
        ).completes();
```

## verify

Shorten expression:

```java
mocker(List.class).never(list -> {}).size();
```

```java
mocker(List.class).times(list -> list.size(), 1).size();
```

Normal expression:

```java
mocker(List.class).verify(list -> {}).never().mock().size();
```

```java
mocker(List.class).verify(list -> list.size()).times(1).mock().size();
```

## Installation

```gradle
repositories {
    // ...
    maven { url "https://jitpack.io" }
}

dependencies {
    testCompile 'com.github.yongjhih:mocker:-SNAPSHOT'
    //testCompile 'com.github.yongjhih:mocker:0.1.2'
}
```

## Bonus: Reuse mocker

```java
Mocker<List> mocker = mocker(List.class).when(list -> list.size()).thenReturn(list -> 3);
Mocker<List> mocker2 = mocker.when(list -> list.toString()).thenReturn(list -> "hello");

List list = mocker.mock();
List list2 = mocker2.mock();

assertEquals(3, list.size());
assertEquals(3, list2.size());

assertNotEquals("hello", list.toString());
assertEquals("hello", list2.toString());
```

## Bonus: asList

```java
List<List> lists = mocker(List.class).when(list -> list.toString()).thenReturn(list -> "hello").asList(3);
assertEquals(lists.size(), 3);
for (List item : lists) assertEquals(item.toString(), "hello");
```

## LICENSE

```
Copyright (C) 2016 8tory, Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
