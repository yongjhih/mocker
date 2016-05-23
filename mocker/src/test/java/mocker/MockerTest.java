/*
 * Copyright (C) 2015 8tory, Inc
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import static mocker.Mocker.mocker;
import static mocker.Mocker.mock;
import mocker.Mocker;
import mocker.Mocker.*;

public class MockerTest {

    @Test
    public void testMockerMock() {
        List mock = mocker(List.class).mock();
        when(mock.size()).thenReturn(3);

        assertEquals(3, mock.size());
    }

    @Test
    public void testMockerOf() {
        List mock = Mocker.of(List.class).mock();
        when(mock.size()).thenReturn(3);

        assertEquals(3, mock.size());
    }

    @Test
    public void testMock() {
        List mock = mock(List.class);
        when(mock.size()).thenReturn(3);

        assertEquals(3, mock.size());
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

        assertEquals(3, mock.size());
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

        assertEquals(3, mock.size());
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

        assertEquals(3, mock.size());
    }

    @Test
    public void testMockerThenResue() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                Mockito.when(list.size()).thenReturn(3);
            }
        });

        List list = mocker.mock();
        List list2 = mocker.mock();

        assertEquals(3, list.size());
        assertEquals(3, list2.size());
        assertNotSame(list, list2);
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

        assertEquals("hello", list.toString());
        assertEquals("hello", list2.toString());
        assertNotEquals(3, list.size());
        assertEquals(3, list2.size());
        assertNotSame(list, list2);
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

        assertEquals("hello", list.toString());
        assertEquals("hello", list2.toString());
        assertNotEquals(3, list.size());
        assertEquals(3, list2.size());
        assertNotSame(list, list2);
    }

    @Test
    public void testMockerThen() {
        List mock = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                Mockito.when(list.size()).thenReturn(3);
            }
        }).mock();

        assertEquals(3, mock.size());
    }

    @Test
    public void testMockerThenThen() {
        List mock = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                Mockito.when(list.size()).thenReturn(3);
            }
        }).then(new Action1<List>() {
            @Override public void call(List list) {
                Mockito.when(list.toString()).thenReturn("hello");
            }
        }).mock();

        assertEquals(3, mock.size());
        assertEquals("hello", mock.toString());
    }

    @Test
    public void testMockerResue() {
        Mocker<List> mocker = mocker(List.class);

        assertNotSame(mocker.mock().hashCode(), mocker.mock().hashCode());
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

        assertNotSame(mocker.mock(), mocker.mock());
        assertNotEquals(mocker.mock().hashCode(), mocker.mock().hashCode());
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

        assertEquals(3, list.size());
        assertNotEquals("hello", list.toString());

        assertEquals(3, list2.size());
        assertEquals("hello", list2.toString());
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

        assertEquals(3, list.size());
        assertNotEquals("hello", list.toString());

        assertEquals(3, list2.size());
        assertEquals("hello", list2.toString());
    }

    @Test
    public void testMockerLift() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
            }
        });

        assertSame(mocker.lift(), mocker.lift());
    }

    @Test
    public void testMockerSafeLift() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
            }
        });

        assertSame(mocker.safeLift(), mocker.safeLift());
    }

    @Test
    public void testMockerThenThenSafeLift() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
            }
        }).then(new Action1<List>() {
            @Override public void call(List list) {
            }
        });

        assertSame(mocker.safeLift(), mocker.safeLift());
    }

    @Test
    public void testMockerLiftSwtich() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
            }
        });
        Mocker<List> mocker2 = mocker.lift(mocker).lift();

        assertSame(mocker.lift(), mocker2);
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

        assertEquals(list.size(), 1);
        assertEquals(list.get(0).toString(), "hello");
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

        assertEquals(list.size(), 3);
        for (List item : list) assertEquals(item.toString(), "hello");
    }

    @Test
    public void testMockerAsListThenWithIndex() { // Then2
        List<List> list = mocker(List.class).then(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                Mockito.when(list.toString()).thenReturn("hello");
            }
        }).asList(3);

        assertEquals(list.size(), 3);
        for (List item : list) assertEquals(item.toString(), "hello");
    }

    @Test
    public void testMockerThenThenWithIndex() { // Then2Then2
        List<List> list = mocker(List.class).then(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                Mockito.when(list.size()).thenReturn(3);
            }
        }).then(new Action2<List, Integer>() {
            @Override public void call(List list, Integer i) {
                Mockito.when(list.toString()).thenReturn("hello");
            }
        }).asList(3);

        for (List item : list) {
            assertEquals(item.size(), 3);
            assertEquals(item.toString(), "hello");
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

        assertEquals(list.size(), 3);
        assertEquals(list.get(0).toString(), "hello");
        assertEquals(list.get(1).toString(), "hello");
        assertEquals(list.get(2).toString(), "hello");
        for (List item : list) assertEquals(item.toString(), "hello");
    }

    @Test
    public void testMockerVerify() {
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
    public void testMockerVerifyNever() {
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

}
