package com.planet_ink.coffee_mud.MOBS;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.util.*;

/* 
   Copyright 2000-2010 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class Falcon extends StdMOB
{
	public String ID(){return "Falcon";}
	public Falcon()
	{
		super();
		Random randomizer = new Random(System.currentTimeMillis());

		Username="a falcon";
		setDescription("a small hunting bird.");
		setDisplayText("A falcon flies nearby.");
		CMLib.factions().setAlignment(this,Faction.ALIGN_NEUTRAL);
		setMoney(0);
		setWimpHitPoint(0);

		baseEnvStats.setWeight(1 + Math.abs(randomizer.nextInt() % 6));


		baseCharStats().setStat(CharStats.STAT_INTELLIGENCE,1);
		baseCharStats().setStat(CharStats.STAT_STRENGTH,10);
		baseCharStats().setStat(CharStats.STAT_DEXTERITY,17);

		baseEnvStats().setDamage(1);
		baseEnvStats().setSpeed(3.0);
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(2);
		baseEnvStats().setArmor(50);
		baseEnvStats().setDisposition(baseEnvStats().disposition()|EnvStats.IS_FLYING);
		baseCharStats().setMyRace(CMClass.getRace("GreatBird"));
		baseCharStats().getMyRace().startRacing(this,false);

		baseState.setHitPoints(CMLib.dice().roll(baseEnvStats().level(),20,baseEnvStats().level()));

		recoverMaxState();
		resetToMaxState();
		recoverEnvStats();
		recoverCharStats();
	}

}
