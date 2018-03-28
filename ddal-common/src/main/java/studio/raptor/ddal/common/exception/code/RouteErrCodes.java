/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"), you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package studio.raptor.ddal.common.exception.code;

import studio.raptor.ddal.common.exception.ErrorCodeDefinition;

/**
 * error code for route exception
 *
 * @author Charley
 * @since 1.0
 */
public interface RouteErrCodes {

  ErrorCodeDefinition ROUTE_400 = new ErrorCodeDefinition("ROUTE", 400,
      "Could not deal with this operate", "");
  ErrorCodeDefinition ROUTE_401 = new ErrorCodeDefinition("ROUTE", 401,
      "There should be one sharding table in sql", "");
  ErrorCodeDefinition ROUTE_402 = new ErrorCodeDefinition("ROUTE", 402,
      "There must be only one table in IUD sql", "");
  ErrorCodeDefinition ROUTE_403 = new ErrorCodeDefinition("ROUTE", 403,
      "Not supported insert sql without sharding columns", "");
  ErrorCodeDefinition ROUTE_404 = new ErrorCodeDefinition("ROUTE", 404,
      "Inert sql of sharding table must have one or more sharding columns", "");
  ErrorCodeDefinition ROUTE_405 = new ErrorCodeDefinition("ROUTE", 405,
      "Update set item should not have sharding column", "");
  ErrorCodeDefinition ROUTE_406 = new ErrorCodeDefinition("ROUTE", 406,
      "Delet sql must have where condition", "");
  ErrorCodeDefinition ROUTE_407 = new ErrorCodeDefinition("ROUTE", 407,
      "EQ operate must have one sharding result or less", "");
  ErrorCodeDefinition ROUTE_408 = new ErrorCodeDefinition("ROUTE", 408,
      "Could not calculate algorithm this table", "");
  ErrorCodeDefinition ROUTE_409 = new ErrorCodeDefinition("ROUTE", 409,
      "One dimension sharding table must have be one sharding column", "");
  ErrorCodeDefinition ROUTE_410 = new ErrorCodeDefinition("ROUTE", 410,
      "Two dimensions sharding table must have be two sharding columns", "");
  ErrorCodeDefinition ROUTE_411 = new ErrorCodeDefinition("ROUTE", 411,
      "This is an unknown sharding", "");
  ErrorCodeDefinition ROUTE_412 = new ErrorCodeDefinition("ROUTE", 412,
      "Can not get virtualDB by name", "");
  ErrorCodeDefinition ROUTE_413 = new ErrorCodeDefinition("ROUTE", 413,
      "Sharding table must have rule", "");
  ErrorCodeDefinition ROUTE_414 = new ErrorCodeDefinition("ROUTE", 414,
      "Table rule must have column", "");
  ErrorCodeDefinition ROUTE_415 = new ErrorCodeDefinition("ROUTE", 415,
      "Table rule must have ruleAlgorithm", "");
  ErrorCodeDefinition ROUTE_416 = new ErrorCodeDefinition("ROUTE", 416,
      "Can not get ruleAlgorithm by name", "");
  ErrorCodeDefinition ROUTE_417 = new ErrorCodeDefinition("ROUTE", 417, "Algorithm must have class",
      "");
  ErrorCodeDefinition ROUTE_420 = new ErrorCodeDefinition("ROUTE", 420,
      "Session must have parse result for RouteTaskHandler", "");
  ErrorCodeDefinition ROUTE_421 = new ErrorCodeDefinition("ROUTE", 421,
      "RouteNodeSet is null or size is 0 in a random method as param", "");
  ErrorCodeDefinition ROUTE_422 = new ErrorCodeDefinition("ROUTE", 422,
      "MultiDimensionCalculate could work without x and y", "");
  ErrorCodeDefinition ROUTE_423 = new ErrorCodeDefinition("ROUTE", 423,
      "Table '%s' not exists in shard-config.xml", "");
  ErrorCodeDefinition ROUTE_424 = new ErrorCodeDefinition("ROUTE", 424,
      "Sharding array size not equals modx*mody", "");
  ErrorCodeDefinition ROUTE_425 = new ErrorCodeDefinition("ROUTE", 425,
      "could not route the sql without table.", "");
  ErrorCodeDefinition ROUTE_426 = new ErrorCodeDefinition("ROUTE", 426,
      "column is null in whereCondition", "");
  ErrorCodeDefinition ROUTE_427 = new ErrorCodeDefinition("ROUTE", 427,
      "Incorrect hash number in hash-range.properties", "");
  ErrorCodeDefinition ROUTE_428 = new ErrorCodeDefinition("ROUTE", 428,
      "Failed to read hash-range.properties", "");
  ErrorCodeDefinition ROUTE_429 = new ErrorCodeDefinition("ROUTE", 429,
      "Invalid range configuration %s", "");
  ErrorCodeDefinition ROUTE_430 = new ErrorCodeDefinition("ROUTE", 430,
      "The size of that table contain actualTable can not large 2", "");
  ErrorCodeDefinition ROUTE_431 = new ErrorCodeDefinition("ROUTE", 431,
      "Unsupported! Tables %s have different shards.", "");
}
