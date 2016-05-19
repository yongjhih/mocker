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
        List<String> mock = mocker(List.class).mock();
        when(mock.size()).thenReturn(3);

        assertEquals(3, mock.size());
    }

    @Test
    public void testMockerOf() {
        List<String> mock = Mocker.of(List.class).mock();
        when(mock.size()).thenReturn(3);

        assertEquals(3, mock.size());
    }

    @Test
    public void testMock() {
        List<String> mock = mock(List.class);
        when(mock.size()).thenReturn(3);

        assertEquals(3, mock.size());
    }

    @Test
    public void testMockerWhenThen() {
        List<String> mock = mocker(List.class).<Integer>when(new Func1<List, Integer>() {
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
    public void testMockerThenRecursive() {
        Mocker<List> mocker = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                Mockito.when(list.size()).thenReturn(3);
            }
        });

        assertEquals(3, mocker.mock().size());
        assertEquals(3, mocker.mock().size());
        assertNotSame(mocker.mock(), mocker.mock());
    }

    @Test
    public void testMockerThen() {
        List<String> mock = mocker(List.class).then(new Action1<List>() {
            @Override public void call(List list) {
                Mockito.when(list.size()).thenReturn(3);
            }
        }).mock();

        assertEquals(3, mock.size());
    }

    @Test
    public void testMockerRecursive() {
        Mocker<List> mocker = mocker(List.class);

        assertNotSame(mocker.mock().hashCode(), mocker.mock().hashCode());
    }

    @Test
    public void testMockerWhenThenRecursive() {
        Mocker<List> mocker = mocker(List.class).<Integer>when(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return list.size();
            }
        }).<Integer>thenReturn(new Func1<List, Integer>() {
            @Override public Integer call(List list) {
                return 3;
            }
        });

        assertEquals(3, mocker.mock().size());
        assertEquals(3, mocker.mock().size());
        assertNotSame(mocker.mock(), mocker.mock());
    }

}
