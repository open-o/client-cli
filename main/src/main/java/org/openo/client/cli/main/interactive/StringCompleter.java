/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.client.cli.main.interactive;

import jline.console.completer.Completer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * String completer for autocomplete commands.
 *
 */
public class StringCompleter implements Completer {
    private final SortedSet<String> strings = new TreeSet<String>();

    /**
     * Public Constructor takes collection of strings.
     *
     * @param strings collection
     */
    public StringCompleter(Collection<String> strings) {
        this.strings.addAll(strings);
    }

    /**
     * Public Constructor takes array of strings.
     *
     * @param strings array of strings
     */
    public void add(String... strings) {
        this.strings.addAll(Arrays.asList(strings));
    }

    /**
     * String completion.
     *
     * @param buffer
     *            string
     * @param cursor
     *            int
     * @param candidates
     *            list
     * @return int
     */
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        if (buffer == null) {
            candidates.addAll(strings);
        } else {
            for (String match : strings.tailSet(buffer)) {
                if (!match.startsWith(buffer)) {
                    break;
                }
                candidates.add(match);
            }
        }
        if (candidates.size() == 1) {
            candidates.set(0, candidates.get(0) + " ");
        }
        return candidates.isEmpty() ? -1 : 0;
    }
}
