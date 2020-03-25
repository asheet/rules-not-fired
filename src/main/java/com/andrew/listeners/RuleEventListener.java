package com.andrew.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.kie.api.definition.KiePackage;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;

public class RuleEventListener implements AgendaEventListener {
	private List<String> rulesFired = new ArrayList<String>();
	private String ruleFlowGroup = "";
	private HashMap<String, ArrayList<String>> rulesInPackage = new HashMap<String, ArrayList<String>>();

	public HashMap<String, ArrayList<String>> getRulesNotFired() {
		return rulesInPackage;
	}

//1. On start up get all the packages
//2. create map of all the packages
//3. get all rules associated to the package
//Per RFG - get rule that fired and remove from the rules in the specific package.
// -Package1
//     -rule
//     -rule
// -Package2
//     -rule
//     -rule
//     -rule
// -Package3
//     -rule
//     -rule
//     -rule


    public List<String> getRulesFired() {
        return this.rulesFired;
    }
    
    public void setRulesFired(List<String> listRules) {
        this.rulesFired = listRules;
    }

    public void afterMatchFired(AfterMatchFiredEvent event) {
		// remove the rule from the list of rules. so we can get the list of rules that were not fired.
		rulesInPackage.get(event.getMatch().getRule().getPackageName()).remove(event.getMatch().getRule().getName());
		System.out.println("Rules in pacakge after " + event.getMatch().getRule().getName() +
		" rule fired: " + rulesInPackage.toString());

		rulesFired.add(event.getMatch().getRule().getName());
    }

	
	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		//ruleFlowGroup = event.getRuleFlowGroup().getName();
	}
	
	public void matchCreated(MatchCreatedEvent event) {
		// 1. Create HashMap for Packages and get all rules
		if(rulesInPackage.isEmpty()) {
			Collection<KiePackage> packages = event.getKieRuntime().getKieBase().getKiePackages();
			for(KiePackage pack: packages) {
				rulesInPackage.put(pack.getName(), new ArrayList<String>());
				
				for(org.kie.api.definition.rule.Rule r : pack.getRules()) {
					rulesInPackage.get(pack.getName()).add(r.getName()); // get the key in the map. add the rules to the arraylist
				}
			}
		}
		System.out.println("All the rules in each package: " + rulesInPackage.toString()); // remove the rule when after match fired.
	}

	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {

	}
	
	public void matchCancelled(MatchCancelledEvent event) {
	}
	
	public void beforeMatchFired(BeforeMatchFiredEvent event) {
	}

	public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
	}

	public void agendaGroupPushed(AgendaGroupPushedEvent event) {
	}

	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
	}

	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
	}
}
