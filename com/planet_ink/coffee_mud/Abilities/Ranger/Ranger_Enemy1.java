package com.planet_ink.coffee_mud.Abilities.Ranger;
import com.planet_ink.coffee_mud.Abilities.StdAbility;
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
@SuppressWarnings("unchecked")
public class Ranger_Enemy1 extends StdAbility
{
	public String ID() { return "Ranger_Enemy1"; }
	public String name(){ return "Favored Enemy 1";}
	public String displayText(){ return "(Enemy of the "+text()+")";}
	public int abstractQuality(){return Ability.QUALITY_BENEFICIAL_SELF;}
	protected int canAffectCode(){return Ability.CAN_MOBS;}
	protected int canTargetCode(){return 0;}
	public boolean isAutoInvoked(){return true;}
	public boolean canBeUninvoked(){return false;}
    public int classificationCode(){return Ability.ACODE_SKILL|Ability.DOMAIN_MARTIALLORE;}

	public String text()
	{
		if(miscText.length()==0)
		{
			if((affected==null)||(!(affected instanceof MOB)))
				return super.text();
			MOB mob=(MOB)affected;
			Vector choices=new Vector();
			for(Enumeration r=CMClass.races();r.hasMoreElements();)
			{
				Race R=(Race)r.nextElement();
				if((!choices.contains(R.racialCategory()))
				&&(CMath.bset(R.availabilityCode(),Area.THEME_FANTASY)))
					choices.addElement(R.racialCategory());
			}
			for(int a=0;a<mob.numLearnedAbilities();a++)
			{
				Ability A=mob.fetchAbility(a);
				if((A instanceof Ranger_Enemy1)
				   &&(((Ranger_Enemy1)A).miscText.length()>0))
					choices.remove(((Ranger_Enemy1)A).miscText);
			}
			for(int a=0;a<mob.numAllEffects();a++)
			{
				Ability A=mob.fetchEffect(a);
				if((A instanceof Ranger_Enemy1)
				   &&(((Ranger_Enemy1)A).miscText.length()>0))
					choices.remove(((Ranger_Enemy1)A).miscText);
			}
			choices.remove("Unique");
			choices.remove("Unknown");
			choices.remove(mob.charStats().getMyRace().racialCategory());
			miscText=(String)choices.elementAt(CMLib.dice().roll(1,choices.size(),-1));
			for(int a=0;a<mob.numLearnedAbilities();a++)
			{
				Ability A=mob.fetchAbility(a);
				if((A!=null)&&(A.ID().equals(ID())))
					((Ranger_Enemy1)A).miscText=miscText;
			}
			for(int a=0;a<mob.numEffects();a++)
			{
				Ability A=mob.fetchEffect(a);
				if((A!=null)&&(A.ID().equals(ID())))
					((Ranger_Enemy1)A).miscText=miscText;
			}
		}
		return super.text();
	}

	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		if((affected==null)||(!(affected instanceof MOB)))
			return;
		MOB mob=(MOB)affected;
		MOB victim=mob.getVictim();
		if((victim!=null)&&(victim.charStats().getMyRace().racialCategory().equals(text())))
		{
			int level=1+adjustedLevel(mob,0);
			double damBonus=CMath.mul(CMath.div(proficiency(),100.0),level);
			double attBonus=CMath.mul(CMath.div(proficiency(),100.0),3*level);
			affectableStats.setAttackAdjustment(affectableStats.attackAdjustment()+(int)Math.round(attBonus));
			affectableStats.setDamage(affectableStats.damage()+(int)Math.round(damBonus));
		}
	}
	public boolean autoInvocation(MOB mob)
	{
		if(mob.charStats().getCurrentClass().ID().equals("Archon"))
			return false;
		return super.autoInvocation(mob);
	}
}
