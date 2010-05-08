package com.planet_ink.coffee_mud.Abilities.Fighter;
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

public class Fighter_CatchProjectile extends FighterSkill
{
	public String ID() { return "Fighter_CatchProjectile"; }
	public String name(){ return "Catch Projectile";}
	public String displayText(){ return "";}
	public int abstractQuality(){return Ability.QUALITY_OK_SELF;}
	protected int canAffectCode(){return Ability.CAN_MOBS;}
	protected int canTargetCode(){return 0;}
	public boolean isAutoInvoked(){return true;}
	public boolean canBeUninvoked(){return false;}
    public int classificationCode(){ return Ability.ACODE_SKILL|Ability.DOMAIN_EVASIVE;}
	public boolean doneThisRound=false;

	public boolean okMessage(Environmental myHost, CMMsg msg)
	{
		if(!super.okMessage(myHost,msg))
			return false;

		if((affected==null)||(!(affected instanceof MOB)))
			return true;

		MOB mob=(MOB)affected;
		if(msg.amITarget(mob)
		&&(!doneThisRound)
		&&(CMLib.flags().aliveAwakeMobileUnbound(mob,true))
		&&(msg.targetMinor()==CMMsg.TYP_WEAPONATTACK)
		&&(msg.tool()!=null)
		&&(msg.tool() instanceof Weapon)
		&&((((Weapon)msg.tool()).weaponClassification()==Weapon.CLASS_RANGED)
		   ||(((Weapon)msg.tool()).weaponClassification()==Weapon.CLASS_THROWN))
		&&(!(msg.tool() instanceof Electronics))
		&&(mob.rangeToTarget()>0)
		&&(mob.fetchEffect("Fighter_ReturnProjectile")==null)
		&&(mob.charStats().getBodyPart(Race.BODY_HAND)>0)
		&&((mob.fetchAbility(ID())==null)||proficiencyCheck(null,-85+mob.charStats().getStat(CharStats.STAT_DEXTERITY)+(3*getXLEVELLevel(mob)),false))
		&&(mob.freeWearPositions(Wearable.WORN_HELD,(short)0,(short)0)>0))
		{
			Item w=(Item)msg.tool();
			if((((Weapon)w).weaponClassification()==Weapon.CLASS_THROWN)
			&&(msg.source().isMine(w)))
			{
				if(!w.amWearingAt(Wearable.IN_INVENTORY))
					CMLib.commands().postRemove(msg.source(),w,true);
				CMLib.commands().postDrop(msg.source(),w,true,false);
			}
			else
			if(((Weapon)w).requiresAmmunition())
			{
				Item neww=CMClass.getItem("GenAmmunition");
				String ammo=((Weapon)w).ammunitionType();
				if(ammo.length()==0) return true;
				if(ammo.endsWith("s"))
					ammo=ammo.substring(0,ammo.length()-1);
				if(("aeiouAEIOU").indexOf(ammo.charAt(0))>=0)
					ammo="an "+ammo;
				else
					ammo="a "+ammo;
				neww.setName(ammo);
				neww.setDisplayText(ammo+" sits here.");
				((Ammunition)neww).setAmmunitionType(ammo);
				neww.setUsesRemaining(1);
				neww.baseEnvStats().setWeight(1);
				neww.setBaseValue(0);
				neww.recoverEnvStats();
				w=neww;
				mob.location().addItemRefuse(neww,CMProps.getIntVar(CMProps.SYSTEMI_EXPIRE_PLAYER_DROP));
			}
			if(mob.location().isContent(w))
			{
				CMMsg msg2=CMClass.getMsg(mob,w,msg.source(),CMMsg.MSG_GET,"<S-NAME> catch(es) the <T-NAME> shot by <O-NAME>!");
				if(mob.location().okMessage(mob,msg2))
				{
					mob.location().send(mob,msg2);
					doneThisRound=true;
					helpProficiency(mob);
					return false;
				}
			}
		}
		return true;
	}

	public boolean tick(Tickable ticking, int tickID)
	{
		if(tickID==Tickable.TICKID_MOB)
			doneThisRound=false;
		return super.tick(ticking,tickID);
	}
}