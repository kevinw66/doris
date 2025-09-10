// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

#include <benchmark/benchmark.h>

#include <vector>

#include <util/utf8_check.h>

namespace doris {
} // namespace doris


static void BM_Utf8Check(benchmark::State& state) {
    size_t len = state.range(0);
    std::string str(len, 'A');
    for (auto _ : state) {
        auto res = doris::validate_utf8(str.c_str(), len);
        benchmark::DoNotOptimize(res);
    }
}

BENCHMARK(BM_Utf8Check)
        ->Unit(benchmark::kNanosecond)
        ->Args({16})
        ->Args({32})
        ->Args({64})
        ->Args({128})
        ->Args({256})
        ->Repetitions(5)
        ->DisplayAggregatesOnly();
