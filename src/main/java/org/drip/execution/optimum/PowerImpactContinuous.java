
package org.drip.execution.optimum;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting analytics/risk, transaction cost analytics,
 *  	asset liability management analytics, capital, exposure, and margin analytics, valuation adjustment
 *  	analytics, and portfolio construction analytics within and across fixed income, credit, commodity,
 *  	equity, FX, and structured products. It also includes auxiliary libraries for algorithm support,
 *  	numerical analysis, numerical optimization, spline builder, model validation, statistical learning,
 *  	and computational support.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three modules:
 *  
 *  - DROP Product Core - https://lakshmidrip.github.io/DROP-Product-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Computational Core - https://lakshmidrip.github.io/DROP-Computational-Core/
 * 
 * 	DROP Product Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Loan Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 *  - Asset Liability Management Analytics
 * 	- Capital Estimation Analytics
 * 	- Exposure Analytics
 * 	- Margin Analytics
 * 	- XVA Analytics
 * 
 * 	DROP Computational Core implements libraries for the following:
 * 	- Algorithm Support
 * 	- Computation Support
 * 	- Function Analysis
 *  - Model Validation
 * 	- Numerical Analysis
 * 	- Numerical Optimizer
 * 	- Spline Builder
 *  - Statistical Learning
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Repo Layout Taxonomy     => https://github.com/lakshmiDRIP/DROP/blob/master/Taxonomy.md
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>PowerImpactContinuous</i> contains the Trading Trajectory generated by the Almgren (2003) Power Impact
 * Scheme under the Criterion of No-Drift. The References are:
 * 
 * <br><br>
 *  <ul>
 * 		<li>
 * 			Almgren, R., and N. Chriss (1999): Value under Liquidation <i>Risk</i> <b>12 (12)</b>
 * 		</li>
 * 		<li>
 * 			Almgren, R. F., and N. Chriss (2000): Optimal Execution of Portfolio Transactions <i>Journal of
 * 				Risk</i> <b>3 (2)</b> 5-39
 * 		</li>
 * 		<li>
 * 			Almgren, R. (2003): Optimal Execution with Nonlinear Impact Functions and Trading-Enhanced Risk
 * 				<i>Applied Mathematical Finance</i> <b>10 (1)</b> 1-18
 * 		</li>
 * 		<li>
 * 			Almgren, R., and N. Chriss (2003): Bidding Principles <i>Risk</i> 97-102
 * 		</li>
 * 		<li>
 * 			Bertsimas, D., and A. W. Lo (1998): Optimal Control of Execution Costs <i>Journal of Financial
 * 				Markets</i> <b>1</b> 1-50
 * 		</li>
 *  </ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ProductCore.md">Product Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/TransactionCostAnalyticsLibrary.md">Transaction Cost Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution/README.md">Optimal Impact/Capture Based Trading Trajectories - Deterministic, Stochastic, Static, and Dynamic</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution/optimum/README.md">Almgren-Chriss Efficient Trading Trajectories</a></li>
 *  </ul>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class PowerImpactContinuous extends org.drip.execution.optimum.EfficientTradingTrajectoryContinuous {
	private double _dblExecutionTimeUpperBound = java.lang.Double.NaN;
	private double _dblHyperboloidBoundaryValue = java.lang.Double.NaN;

	/**
	 * Construct the Standard PowerImpactContinuous Instance
	 * 
	 * @param dblExecutionTime The Execution Time
	 * @param dblTransactionCostExpectation The Expected Transaction Cost
	 * @param dblTransactionCostVariance The Variance of the Transaction Cost
	 * @param dblCharacteristicTime The Optimal Trajectory's "Characteristic" Time
	 * @param dblExecutionTimeUpperBound The Optimal Trajectory's Execution Time Upper Bound (if it exists)
	 * @param dblHyperboloidBoundaryValue The Hyperboloid Boundary Value
	 * @param dblMarketPower The Dimension-less Relative Market Impact
	 * @param r1ToR1Holdings The Optimal Trajectory R^1 To R^1 Holdings Function
	 * @param r1ToR1TransactionCostExpectation The Transaction Cost Expectation Function
	 * @param r1ToR1TransactionCostVariance The Transaction Cost Variance Function
	 * 
	 * @return The Standard PowerImpactContinuous Instance
	 */

	public static PowerImpactContinuous Standard (
		final double dblExecutionTime,
		final double dblTransactionCostExpectation,
		final double dblTransactionCostVariance,
		final double dblCharacteristicTime,
		final double dblExecutionTimeUpperBound,
		final double dblHyperboloidBoundaryValue,
		final double dblMarketPower,
		final org.drip.function.definition.R1ToR1 r1ToR1Holdings,
		final org.drip.function.definition.R1ToR1 r1ToR1TransactionCostExpectation,
		final org.drip.function.definition.R1ToR1 r1ToR1TransactionCostVariance)
	{
		if (null == r1ToR1Holdings) return null;

		try {
			org.drip.function.definition.R1ToR1 r1ToR1TradeRate = new org.drip.function.definition.R1ToR1
				(null) {
				@Override public double evaluate (
					final double dblVariate)
					throws java.lang.Exception
				{
					return r1ToR1Holdings.derivative (dblVariate, 1);
				}
			};

			return new PowerImpactContinuous (dblExecutionTime, dblTransactionCostExpectation,
				dblTransactionCostVariance, dblCharacteristicTime, dblExecutionTimeUpperBound,
					dblHyperboloidBoundaryValue, dblMarketPower, r1ToR1Holdings, r1ToR1TradeRate,
						r1ToR1TransactionCostExpectation, r1ToR1TransactionCostVariance);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * PowerImpactContinuous Constructor
	 * 
	 * @param dblExecutionTime The Execution Time
	 * @param dblTransactionCostExpectation The Expected Transaction Cost
	 * @param dblTransactionCostVariance The Variance of the Transaction Cost
	 * @param dblCharacteristicTime The Optimal Trajectory's "Characteristic" Time
	 * @param dblExecutionTimeUpperBound The Optimal Trajectory's Execution Time Upper Bound (if it exists)
	 * @param dblHyperboloidBoundaryValue The Hyperboloid Boundary Value
	 * @param dblMarketPower The Dimension-less Relative Market Impact
	 * @param r1ToR1Holdings The Optimal Trajectory R^1 To R^1 Holdings Function
	 * @param r1ToR1TradeRate The Optimal Trajectory R^1 To R^1 Trade Rate Function
	 * @param r1ToR1TransactionCostExpectation The Transaction Cost Expectation Function
	 * @param r1ToR1TransactionCostVariance The Transaction Cost Variance Function
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public PowerImpactContinuous (
		final double dblExecutionTime,
		final double dblTransactionCostExpectation,
		final double dblTransactionCostVariance,
		final double dblCharacteristicTime,
		final double dblExecutionTimeUpperBound,
		final double dblHyperboloidBoundaryValue,
		final double dblMarketPower,
		final org.drip.function.definition.R1ToR1 r1ToR1Holdings,
		final org.drip.function.definition.R1ToR1 r1ToR1TradeRate,
		final org.drip.function.definition.R1ToR1 r1ToR1TransactionCostExpectation,
		final org.drip.function.definition.R1ToR1 r1ToR1TransactionCostVariance)
		throws java.lang.Exception
	{
		super (dblExecutionTime, dblTransactionCostExpectation, dblTransactionCostVariance,
			dblCharacteristicTime, dblMarketPower, r1ToR1Holdings, r1ToR1TradeRate,
				r1ToR1TransactionCostExpectation, r1ToR1TransactionCostVariance);

		if (!org.drip.numerical.common.NumberUtil.IsValid (_dblHyperboloidBoundaryValue =
			dblHyperboloidBoundaryValue))
			throw new java.lang.Exception ("PowerImpactContinuous Constructor => Invalid Inputs");

		_dblExecutionTimeUpperBound = dblExecutionTimeUpperBound;
	}

	/**
	 * Retrieve the Optimal Trajectory Execution Time Upper Bound (if it exists)
	 * 
	 * @return The Optimal Trajectory Execution Time Upper Bound (if it exists)
	 */

	public double executionTimeUpperBound()
	{
		return _dblExecutionTimeUpperBound;
	}

	/**
	 * Retrieve the Optimal Trajectory Hyperboloid Boundary Value
	 * 
	 * @return The Optimal Trajectory Hyperboloid Boundary Value
	 */

	public double hyperboloidBoundaryValue()
	{
		return _dblHyperboloidBoundaryValue;
	}
}
