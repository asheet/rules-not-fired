package com.andrew.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.andrew.model.Person;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;

public class RuleEventListenerTest {
	private KieSession kSession;
	private RuleEventListener eventListener = new RuleEventListener();

	@Before
	public void initSession() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		kSession = kContainer.newKieSession("testSession");
	}

	@After
	public void closeSession() {
		kSession.dispose();
	}

	@Test
	public void testRulesLoggerListener() {
		kSession.addEventListener(eventListener);
		Person person = new Person();
		// insert all facts
		kSession.insert(person);

		// fire rules
		int nbRulesFired = kSession.fireAllRules();

		assertEquals(1, nbRulesFired);

		// grab fact out of ksession
		Collection<FactHandle> facts = kSession.getFactHandles(new ObjectFilter() {
			public boolean accept(Object obj) {
				return obj instanceof Person;
			}
		});

		assertEquals("Andrew One", person.getFullName());
		assertEquals(1, facts.size());
		assertEquals(1, eventListener.getRulesFired().size());
		String result = eventListener.getRulesFired().iterator().next();
		
		assertEquals("Listener Test", result);
		HashMap<String, ArrayList<String>> rulesNotFiredMap = eventListener.getRulesNotFired();
		ArrayList<String> notFiredRulesInListenerPackage = rulesNotFiredMap.get("com.andrew.listeners");
		assertEquals(3, notFiredRulesInListenerPackage.size());
		assertEquals(false, notFiredRulesInListenerPackage.contains("ListenerTest"));
		ArrayList<String> notFiredRulesInDifferentPackage = rulesNotFiredMap.get("com.andrew.different_package");
		assertEquals(1, notFiredRulesInDifferentPackage.size());
	}
}
