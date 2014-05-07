/**
 * Copyright (c) 2002-2014 "Neo Technology,"
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
package org.neo4j.cypher.internal.compiler.v2_1.planner.logical.plans

import org.neo4j.cypher.internal.compiler.v2_1.ast.Expression
import org.neo4j.cypher.internal.compiler.v2_1.planner.QueryGraph

case class DirectedRelationshipByIdSeek(idName: IdName,
                                        relIds: Seq[Expression],
                                        startNode: IdName,
                                        endNode: IdName)
                                       (val pattern: PatternRelationship,
                                        val solvedPredicates: Seq[Expression] = Seq.empty) extends LogicalLeafPlan {

  def solved = DirectedRelationshipByIdSeekPlan(idName, relIds, startNode, endNode, pattern, solvedPredicates).solved

  def availableSymbols: Set[IdName] = Set(idName, startNode, endNode)
}

object DirectedRelationshipByIdSeekPlan {
  def apply(idName: IdName,
            relIds: Seq[Expression],
            startNode: IdName,
            endNode: IdName,
            pattern: PatternRelationship,
            solvedPredicates: Seq[Expression] = Seq.empty) =
    QueryPlan(
      DirectedRelationshipByIdSeek(idName, relIds, startNode, endNode)(pattern, solvedPredicates),
      QueryGraph
        .empty
        .addPatternRel(pattern)
    )
}