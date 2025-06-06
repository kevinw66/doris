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

suite("analyze_agg") {
    sql """DROP TABLE IF EXISTS t1"""
    sql """DROP TABLE IF EXISTS t2"""

    sql """SET enable_fallback_to_original_planner=false"""
    sql """SET enable_nereids_planner=true"""

    sql """    
        create table t1
        (
           id INT,
           a VARCHAR(32)
        )ENGINE = OLAP
        UNIQUE KEY(id)
        DISTRIBUTED BY HASH(id) BUCKETS 30
        PROPERTIES (
            "replication_allocation" = "tag.location.default: 1"
        );
    """
    sql """
        create table t2
        (
            id INT,
            b VARCHAR(30),
            c INT default '0',
            d VARCHAR(30),
            e VARCHAR(32),
            a VARCHAR(32),
            f VARCHAR(32),
            g DECIMAL(9, 3)
        )ENGINE = OLAP
        UNIQUE KEY(id)
        DISTRIBUTED BY HASH(id) BUCKETS 30
        PROPERTIES (
            "replication_allocation" = "tag.location.default: 1"
        );
    """
    qt_sql """
        
        SELECT
               tt.d,
               tt2.c
        FROM  t1 t
                 LEFT JOIN t2 tt
                           ON tt.f = t.a
                               and tt.b = 'EA'
                 left join t2 tt2
                           on tt2.f = t.a
                               and tt2.b = 'CS'
        group by
                 tt.d,
                 tt2.d,
                 tt2.c;
    """

    test {
        sql "select count(distinct t2.b), variance(distinct t2.c) from t2"
        exception "variance(DISTINCT c#2) can't support multi distinct."
    }

    // should not bind g /g in group by again, otherwise will throw exception
    sql "select g / g as nu, sum(c) from t2 group by nu"
    sql """
            select
                1,
                id / (select max(id) from t2)  as 'x',
                count(distinct c) as 'y'
            from
                t2
            group by
                1,
                x
        """

    sql "drop table if exists test_sum0_multi_distinct_with_group_by"
    sql "create table test_sum0_multi_distinct_with_group_by (a int, b int, c int) distributed by hash(a) properties('replication_num'='1');"
    sql """
    INSERT INTO test_sum0_multi_distinct_with_group_by VALUES 
    (1, NULL, 3), (2, NULL, 5), (3, NULL, 7),
    (4,5,6),(4,5,7),(4,5,8),
    (5,0,0),(5,0,0),(5,0,0); 
    """
    qt_test_sum0 "select sum0(distinct b),sum(distinct c) from test_sum0_multi_distinct_with_group_by group by a order by 1,2"
    qt_test_sum0_all_null "select sum0(distinct b),sum(distinct c) from test_sum0_multi_distinct_with_group_by where a in (1,2,3) group by a order by 1,2"
}