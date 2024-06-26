/*
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
package io.trino.plugin.kafka;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static io.airlift.slice.SizeOf.instanceSize;
import static java.lang.Math.min;

/**
 * @param begin inclusive
 * @param end exclusive
 */
public record Range(long begin, long end)
{
    private static final int INSTANCE_SIZE = instanceSize(Range.class);

    public List<Range> partition(int partitionSize)
    {
        ImmutableList.Builder<Range> partitions = ImmutableList.builder();
        long position = begin;
        while (position <= end) {
            partitions.add(new Range(position, min(position + partitionSize, end)));
            position += partitionSize;
        }
        return partitions.build();
    }

    public long retainedSizeInBytes()
    {
        return INSTANCE_SIZE;
    }
}
