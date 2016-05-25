/*
 * Copyright (C) 2016 8tory, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mocker;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static mocker.Mocker.mocker;
import static mocker.Mocker.mock;
import mocker.Mocker;
import mocker.Mocker.*;

public class MockerTest {

    @Test
    public void testMockerMock() {
        List mock = mocker(List.class).mock();
        when(mock.size()).thenReturn(3);

        assertThat(mock.size()).isEqualTo(3);
    }

    @Test
    public void testMockerOf() {
        List mock = Mocker.of(List.class).mock();
        when(mock.size()).thenReturn(3);

        assertThat(mock.size()).isEqualTo(3);
    }

    @Test
    public void testMock() {
        List mock = mock(List.class);
        when(mock.size()).thenReturn(3);

        assertThat(mock.size()).isEqualTo(3);
    }

    @Test
    public void testMockerWhenThen() {
        List mock = mocker(List.class).<Integer>when(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        }).mock();

        assertThat(mock.size()).isEqualTo(3);
    }

    @Test
    public void testMockerWhen2Then() {
        List mock = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        }).mock();

        assertThat(mock.size()).isEqualTo(3);
    }

    @Test
    public void testMockerWhenThen2() {
        List mock = mocker(List.class).<Integer>when(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return 3;
            }
        }).mock();

        assertThat(mock.size()).isEqualTo(3);
    }

    @Test
    public void testMockerThenResue() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                when(list.size()).thenReturn(3);
            }
        });

        List list = mocker.mock();
        List list2 = mocker.mock();

        assertThat(list.size()).isEqualTo(3);
        assertThat(list2.size()).isEqualTo(3);
        assertThat(list).isNotSameAs(list2);
    }

    @Test
    public void testMockerWhenThenWhen2Then2() {
        Mocker<List> mocker = mocker(List.class).<String>when(new Func1<List, String>() {
            @Override public String call(List list) {
                return list.toString();
            }
        }).<String>thenReturn(new Func1<List, String>() {
            @Override public String call(List list) {
                return "hello";
            }
        });

        List list = mocker.mock();
        List list2 = mocker.<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        }).mock();

        assertThat(list.toString()).isEqualTo("hello");
        assertThat(list2.toString()).isEqualTo("hello");
        assertThat(list.size()).isNotEqualTo(3);
        assertThat(list2.size()).isEqualTo(3);
        assertThat(list).isNotSameAs(list2);
        //assertNotSame(list, list2);
    }

    @Test
    public void testMockerWhen2Then2Resue() {
        Mocker<List> mocker = mocker(List.class).<String>when(new Func2<List, Integer, String>() {
            @Override public String call(List list, Integer i) {
                return list.toString();
            }
        }).<String>thenReturn(new Func2<List, Integer, String>() {
            @Override public String call(List list, Integer i) {
                return "hello";
            }
        });

        List list = mocker.mock();
        List list2 = mocker.<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        }).mock();

        assertThat(list.toString()).isEqualTo("hello");
        assertThat(list2.toString()).isEqualTo("hello");
        assertThat(list.size()).isNotEqualTo(3);
        assertThat(list2.size()).isEqualTo(3);
        assertThat(list).isNotSameAs(list2);
    }

    @Test
    public void testMockerThen() {
        List mock = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                when(list.size()).thenReturn(3);
            }
        }).mock();

        assertThat(mock.size()).isEqualTo(3);
    }

    @Test
    public void testMockerThenThen() {
        List mock = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                when(list.size()).thenReturn(3);
            }
        }).then(new Action1<List>() {
            @Override public void call(List list) {
                when(list.toString()).thenReturn("hello");
            }
        }).mock();

        assertThat(mock.size()).isEqualTo(3);
        assertThat(mock.toString()).isEqualTo("hello");
    }

    @Test
    public void testMockerResue() {
        Mocker<List> mocker = mocker(List.class);

        assertThat(mocker.mock().hashCode()).isNotSameAs(mocker.mock().hashCode());
    }

    @Test
    public void testMockerRemock() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        assertThat(mocker.mock().hashCode()).isNotSameAs(mocker.mock().hashCode());
        assertThat(mocker.mock().hashCode()).isNotEqualTo(mocker.mock().hashCode());
    }

    @Test
    public void testMockerReuseSequense() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        List list = mocker.mock();

        Mocker<List> mocker2 = mocker.<String>when(new Func1<List, String>() {
            @Override public String call(List list) {
                return list.toString();
            }
        }).<String>thenReturn(new Func1<List, String>() {
            @Override public String call(List list) {
                return "hello";
            }
        });

        List list2 = mocker2.mock();

        assertThat(list.size()).isEqualTo(3);
        assertThat(list2.size()).isEqualTo(3);

        assertThat(list.toString()).isNotEqualTo("hello");
        assertThat(list2.toString()).isEqualTo("hello");
    }

    @Test
    public void testMockerReuseParallel() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        Mocker<List> mocker2 = mocker.<String>when(new Func1<List, String>() {
            @Override public String call(List list) {
                return list.toString();
            }
        }).<String>thenReturn(new Func1<List, String>() {
            @Override public String call(List list) {
                return "hello";
            }
        });

        List list = mocker.mock();
        List list2 = mocker2.mock();

        assertThat(list.size()).isEqualTo(3);
        assertThat(list2.size()).isEqualTo(3);

        assertThat(list.toString()).isNotEqualTo("hello");
        assertThat(list2.toString()).isEqualTo("hello");
    }

    @Test
    public void testMockerLift() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        });

        assertThat(mocker.lift()).isSameAs(mocker.lift());
    }

    @Test
    public void testMockerSafeLift() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        });

        assertThat(mocker.safeLift()).isSameAs(mocker.safeLift());
    }

    @Test
    public void testMockerThenThenSafeLift() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        }).then(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        });

        assertThat(mocker.safeLift()).isSameAs(mocker.safeLift());
    }

    @Test
    public void testMockerLiftSwtich() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        });
        Mocker<List> mocker2 = mocker.lift(mocker).lift();

        assertThat(mocker.lift()).isSameAs(mocker2);
    }

    @Test
    public void testMockerAsList() {
        List<List> list = mocker(List.class).<String>when(new Func1<List, String>() {
            @Override public String call(List list) {
                return list.toString();
            }
        }).<String>thenReturn(new Func1<List, String>() {
            @Override public String call(List list) {
                return "hello";
            }
        }).asList();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).toString()).isEqualTo("hello");
    }

    @Test
    public void testMockerAsListWithIndex() { // When2Then2
        List<List> list = mocker(List.class).<String>when(new Func2<List, Integer, String>() {
            @Override public String call(List list, Integer i) {
                return list.toString();
            }
        }).<String>thenReturn(new Func2<List, Integer, String>() {
            @Override public String call(List list, Integer i) {
                return "hello";
            }
        }).asList(3);

        assertThat(list.size()).isEqualTo(3);
        for (List item : list) assertThat(item.toString()).isEqualTo("hello");
    }

    @Test
    public void testMockerAsListThenWithIndex() { // Then2
        List<List> list = mocker(List.class).then(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                when(list.toString()).thenReturn("hello");
            }
        }).asList(3);

        assertThat(list.size()).isEqualTo(3);
        for (List item : list) assertThat(item.toString()).isEqualTo("hello");
    }

    @Test
    public void testMockerThenThenWithIndex() { // Then2Then2
        List<List> list = mocker(List.class).then(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                when(list.size()).thenReturn(3);
            }
        }).then(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                when(list.toString()).thenReturn("hello");
            }
        }).asList(3);

        for (List item : list) {
            assertThat(item.size()).isEqualTo(3);
            assertThat(item.toString()).isEqualTo("hello");
        }
    }

    @Test
    public void testMockerAsListMany() {
        List<List> list = mocker(List.class).<String>when(new Func1<List, String>() {
            @Override public String call(List list) {
                return list.toString();
            }
        }).<String>thenReturn(new Func1<List, String>() {
            @Override public String call(List list) {
                return "hello";
            }
        }).asList(3);

        assertThat(list.size()).isEqualTo(3);
        for (List item : list) assertThat(item.toString()).isEqualTo("hello");
    }

    @Test
    public void testMockerWhenThenVerify() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.verify(new Action1<List>() {
            @Override public void call(List list) {
                list.size();
            }
        }).mock().size();
    }

    @Test
    public void testMockerVerifyTimes() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.verify(new Action1<List>() {
            @Override public void call(List list) {
                list.size();
                list.size();
                list.size();
            }
        }).times(3).mock().size();
    }

    @Test
    public void testMockerVerifyAtLeast() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.verify(new Action1<List>() {
            @Override public void call(List list) {
                list.size();
                list.size();
                list.size();
            }
        }).atLeast(3).mock().size();
    }

    @Test
    public void testMockerVerifyAtMost() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });


        mocker.verify(new Action1<List>() {
            @Override public void call(List list) {
                list.size();
                list.size();
                list.size();
            }
        }).atMost(3).mock().size();
    }

    @Test
    public void testMockerWhenThenVerifyNever() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.verify(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        }).never().mock().size();
    }

    @Test
    public void testMockerVerifyAtLeastOnce() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.verify(new Action1<List>() {
            @Override public void call(List list) {
                list.size();
                list.size();
                list.size();
            }
        }).atLeastOnce().mock().size();
    }

    @Test
    public void testMockerVerifyTimesWithIndex() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.verify(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                list.size();
                list.size();
                list.size();
            }
        }).times(3).mock().size();
    }

    @Test
    public void testMockerVerifyWithIndex() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.verify(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                list.size();
            }
        }).mock().size();
    }

    @Test
    public void testMockerNever() {
        mocker(List.class).never(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        }).size();
    }

    @Test
    public void testMockerWhenThenNever() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func2<List, Integer, Integer>() {
            @Override public Integer call(List list, Integer i) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        mocker.never(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        }).size();
    }

    @Test
    public void testMockerVerifyNever() {
        mocker(List.class).verify(new Action1<List>() {
            @Override public void call(List list) {
                // do nothing
            }
        }).never();
    }

    @Test
    public void testMockerVerify() {
        mocker(List.class).verify(new Action1<List>() {
            @Override public void call(List list) {
                list.size();
            }
        }).mock().size();
    }

    @Test
    public void testMockerTimes() {
        mocker(List.class).times(new Action1<List>() {
            @Override public void call(List list) {
                list.size();
            }
        }, 1).size();
    }

}
