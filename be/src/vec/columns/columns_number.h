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
// This file is copied from
// https://github.com/ClickHouse/ClickHouse/blob/master/src/Columns/ColumnsNumber.h
// and modified by Doris

#pragma once

#include "vec/columns/column_decimal.h"
#include "vec/columns/column_vector.h"
#include "vec/core/types.h"

namespace doris::vectorized {

using ColumnDecimal32 = ColumnDecimal<Decimal32>;
using ColumnDecimal64 = ColumnDecimal<Decimal64>;
using ColumnDecimal128V2 = ColumnDecimal<Decimal128V2>;
using ColumnDecimal128V3 = ColumnDecimal<Decimal128V3>;
using ColumnDecimal256 = ColumnDecimal<Decimal256>;

} // namespace doris::vectorized
