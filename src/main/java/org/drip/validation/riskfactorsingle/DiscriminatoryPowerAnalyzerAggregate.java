
package org.drip.validation.riskfactorsingle;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
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
 * <i>DiscriminatoryPowerAnalyzerAggregate</i> implements the Discriminatory Power Analyzer for the given
 * Sample across the One/More Hypothesis and Multiple Events.
 *
 *  <br><br>
 *  <ul>
 *  	<li>
 *  		Anfuso, F., D. Karyampas, and A. Nawroth (2017): A Sound Basel III Compliant Framework for
 *  			Back-testing Credit Exposure Models
 *  			https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2264620 <b>eSSRN</b>
 *  	</li>
 *  	<li>
 *  		Diebold, F. X., T. A. Gunther, and A. S. Tay (1998): Evaluating Density Forecasts with
 *  			Applications to Financial Risk Management, International Economic Review 39 (4) 863-883
 *  	</li>
 *  	<li>
 *  		Kenyon, C., and R. Stamm (2012): Discounting, LIBOR, CVA, and Funding: Interest Rate and Credit
 *  			Pricing, Palgrave Macmillan
 *  	</li>
 *  	<li>
 *  		Wikipedia (2018): Probability Integral Transform
 *  			https://en.wikipedia.org/wiki/Probability_integral_transform
 *  	</li>
 *  	<li>
 *  		Wikipedia (2019): p-value https://en.wikipedia.org/wiki/P-value
 *  	</li>
 *  </ul>
 *
 *  <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ModelValidationAnalyticsLibrary.md">Model Validation Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/validation/README.md">Risk Factor and Hypothesis Validation, Evidence Processing, and Model Testing</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/validation/riskfactorsingle/README.md">Single Risk Factor Aggregate Tests</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class DiscriminatoryPowerAnalyzerAggregate
{
	private org.drip.validation.distance.GapTestSetting _gapTestSetting = null;
	private org.drip.validation.riskfactorsingle.EventAggregationWeightFunction _eventAggregationWeightFunction =
		null;

	private java.util.Map<java.lang.String, org.drip.validation.hypothesis.ProbabilityIntegralTransform>
		_eventSamplePITMap = new
			org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.validation.hypothesis.ProbabilityIntegralTransform>();

	/**
	 * DiscriminatoryPowerAnalyzerAggregate Constructor
	 * 
	 * @param eventSamplePITMap Event Sample PIT Map
	 * @param gapTestSetting The Distance Gap Test Setting
	 * @param eventAggregationWeightFunction Event Aggregation Weight Function
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public DiscriminatoryPowerAnalyzerAggregate (
		final java.util.Map<java.lang.String, org.drip.validation.hypothesis.ProbabilityIntegralTransform>
			eventSamplePITMap,
		final org.drip.validation.distance.GapTestSetting gapTestSetting,
		final org.drip.validation.riskfactorsingle.EventAggregationWeightFunction eventAggregationWeightFunction)
		throws java.lang.Exception
	{
		if (null == (_eventSamplePITMap = eventSamplePITMap) || 0 == _eventSamplePITMap.size() ||
			null == (_gapTestSetting = gapTestSetting) ||
			null == (_eventAggregationWeightFunction = eventAggregationWeightFunction))
		{
			throw new java.lang.Exception
				("DiscriminatoryPowerAnalyzerAggregate Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Gap Test Setting
	 * 
	 * @return The Gap Test Setting
	 */

	public org.drip.validation.distance.GapTestSetting gapTestSetting()
	{
		return _gapTestSetting;
	}

	/**
	 * Retrieve the Event Aggregation Weight Function
	 *
	 * @return The Event Aggregation Weight Function
	 */

	public org.drip.validation.riskfactorsingle.EventAggregationWeightFunction eventAggregationWeightFunction()
	{
		return _eventAggregationWeightFunction;
	}

	/**
	 * Retrieve the Event Sample PIT Map
	 * 
	 * @return The Event Sample PIT Map
	 */

	public java.util.Map<java.lang.String, org.drip.validation.hypothesis.ProbabilityIntegralTransform>
		eventSamplePITMap()
	{
		return _eventSamplePITMap;
	}

	private org.drip.validation.riskfactorsingle.GapTestOutcomeAggregate eventOutcomeAggregate (
		final java.lang.String hypothesisID,
		final java.util.Map<java.lang.String, org.drip.validation.evidence.Ensemble> eventEnsembleMap)
	{
		double distanceAggregate = 0.;

		java.util.Map<java.lang.String, org.drip.validation.distance.GapTestOutcome> eventOutcomeMap = new
			org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.validation.distance.GapTestOutcome>();

		try
		{
			for (java.util.Map.Entry<java.lang.String, org.drip.validation.evidence.Ensemble> eventEnsemble :
				eventEnsembleMap.entrySet())
			{
				java.lang.String eventID = eventEnsemble.getKey();

				if (!_eventSamplePITMap.containsKey (eventID))
				{
					return null;
				}

				DiscriminatoryPowerAnalyzer discriminatoryPowerAnalyzer = new DiscriminatoryPowerAnalyzer
				(
					_eventSamplePITMap.get (eventID),
					_gapTestSetting
				);

				org.drip.validation.distance.GapTestOutcome gapTestOutcome =
					discriminatoryPowerAnalyzer.gapTest (eventEnsemble.getValue());

				if (null == gapTestOutcome)
				{
					return null;
				}

				distanceAggregate = distanceAggregate + gapTestOutcome.distance() *
					_eventAggregationWeightFunction.loading (eventID);

				eventOutcomeMap.put (
					eventID,
					gapTestOutcome
				);
			}

			return new org.drip.validation.riskfactorsingle.GapTestOutcomeAggregate (
				eventOutcomeMap,
				distanceAggregate
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Generate the Hypotheses Outcome Suite Aggregate for the specified Hypothesis Suite Aggregate
	 * 
	 * @param hypothesisSuiteAggregate The Hypothesis Suite Aggregate
	 * 
	 * @return The Suite of Gap Test Outcomes
	 */

	public org.drip.validation.riskfactorsingle.HypothesisOutcomeSuiteAggregate hypothesisGapTest (
		final org.drip.validation.riskfactorsingle.HypothesisSuiteAggregate hypothesisSuiteAggregate)
	{
		if (null == hypothesisSuiteAggregate)
		{
			return null;
		}

		java.util.Map<java.lang.String, java.util.Map<java.lang.String,
			org.drip.validation.evidence.Ensemble>> hypothesisEventMap =
				hypothesisSuiteAggregate.hypothesisEventMap();

		if (0 == hypothesisEventMap.size())
		{
			return null;
		}

		org.drip.validation.riskfactorsingle.HypothesisOutcomeSuiteAggregate hypothesisOutcomeSuiteAggregate = new
			org.drip.validation.riskfactorsingle.HypothesisOutcomeSuiteAggregate();

		for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
			org.drip.validation.evidence.Ensemble>> hypothesisEvent : hypothesisEventMap.entrySet())
		{
			java.lang.String hypothesisID = hypothesisEvent.getKey();

			if (!hypothesisOutcomeSuiteAggregate.add (
				hypothesisID,
				eventOutcomeAggregate (
					hypothesisID,
					hypothesisEvent.getValue()
				)
			))
			{
				return null;
			}
		}

		return hypothesisOutcomeSuiteAggregate;
	}
}
