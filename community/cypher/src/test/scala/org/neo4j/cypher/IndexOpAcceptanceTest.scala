/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher

import org.scalatest.Assertions
import org.junit.Test
import org.neo4j.kernel.api.exceptions.schema.{NoSuchIndexException, DropIndexFailureException}

class IndexOpAcceptanceTest extends ExecutionEngineHelper with StatisticsChecker with Assertions {
  @Test def createIndex() {
    // WHEN
    parseAndExecute("CREATE INDEX ON :Person(name)")

    // THEN
    assertInTx(List(List("name")) === graph.indexPropsForLabel("Person"))
  }

  @Test def dropIndex() {
    // GIVEN
    parseAndExecute("CREATE INDEX ON :Person(name)")

    // WHEN
    parseAndExecute("DROP INDEX ON :Person(name)")

    // THEN
    assertInTx(List.empty[List[String]] === graph.indexPropsForLabel("Person"))
  }

  @Test def drop_index_that_does_not_exist() {
    // WHEN
    val e = intercept[CypherExecutionException](parseAndExecute("DROP INDEX ON :Person(name)"))
    assert(e.getCause.isInstanceOf[DropIndexFailureException])
    assert(e.getCause.getCause.isInstanceOf[NoSuchIndexException])
  }
}