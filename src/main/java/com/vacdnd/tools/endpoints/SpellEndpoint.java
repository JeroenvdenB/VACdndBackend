package com.vacdnd.tools.endpoints;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vacdnd.tools.domain.Spell;
import com.vacdnd.tools.util.SpellFormatter;

@RestController
public class SpellEndpoint {

	@PostMapping("addSpell")
	public void addSpell(@RequestBody Spell spell) {
		String formattedSpell = SpellFormatter.formatSpell(spell);
		spell.SpellUploader(formattedSpell);

	}

}
