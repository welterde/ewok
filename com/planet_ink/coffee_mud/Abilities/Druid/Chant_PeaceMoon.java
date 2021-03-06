package com.planet_ink.coffee_mud.Abilities.Druid;
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
public class Chant_PeaceMoon extends Chant
{
	public String ID() { return "Chant_PeaceMoon"; }
	public String name(){ return "Peace Moon";}
	public String displayText(){return "(Peace Moon)";}
	public int abstractQuality(){return Ability.QUALITY_INDIFFERENT;}
	protected int canAffectCode(){return CAN_MOBS|CAN_ROOMS;}
	protected int canTargetCode(){return 0;}
	public int classificationCode(){return Ability.ACODE_CHANT|Ability.DOMAIN_MOONALTERING;}

	public void unInvoke()
	{
		// undo the affects of this spell
		if((affected==null)||(!(affected instanceof MOB)))
		{
			if((affected instanceof Room)&&(canBeUninvoked()))
				((Room)affected).showHappens(CMMsg.MSG_OK_VISUAL,"The peace moon sets.");
			super.unInvoke();
			return;
		}
		MOB mob=(MOB)affected;
		if(canBeUninvoked())
			mob.tell("You are no longer under the peace moon.");

		super.unInvoke();

	}

	public boolean okMessage(Environmental myHost, CMMsg msg)
	{
		if(affected instanceof Room)
		if((CMath.bset(msg.sourceCode(),CMMsg.MASK_MALICIOUS))
		||(CMath.bset(msg.targetCode(),CMMsg.MASK_MALICIOUS))
		||(CMath.bset(msg.othersCode(),CMMsg.MASK_MALICIOUS)))
		{
			if((msg.source()!=null)
			   &&(msg.target()!=null)
			   &&(msg.source()!=msg.target()))
			{
				msg.source().tell("Nah, you feel too peaceful under that bright moon.");
				MOB victim=msg.source().getVictim();
				if(victim!=null) victim.makePeace();
				msg.source().makePeace();
			}
			msg.modify(msg.source(),msg.target(),msg.tool(),CMMsg.NO_EFFECT,"",CMMsg.NO_EFFECT,"",CMMsg.NO_EFFECT,"");
			return false;
		}
		return super.okMessage(myHost,msg);
	}

	public boolean tick(Tickable ticking, int tickID)
	{
		if(!super.tick(ticking,tickID)) return false;
		if(affected==null) return false;
		if(affected instanceof MOB)
		{
			MOB mob=(MOB)affected;
			if(mob.location().fetchEffect(ID())==null)
				unInvoke();
			if(mob.isInCombat()) mob.makePeace();
		}
		else
		if(affected instanceof Room)
		{
			Room room=(Room)affected;
			if(!room.getArea().getClimateObj().canSeeTheMoon(room,this))
				unInvoke();
			else
			for(int i=0;i<room.numInhabitants();i++)
			{
				MOB M=room.fetchInhabitant(i);
				if((M!=null)&&(M.fetchEffect(ID())==null))
				{
					Ability A=(Ability)copyOf();
					M.addEffect(A);
				}
			}
		}
		return true;
	}

	public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel)
	{
		Room target=mob.location();
		if(target==null) return false;
		if(!target.getArea().getClimateObj().canSeeTheMoon(target,null))
		{
			mob.tell("You must be able to see the moon for this magic to work.");
			return false;
		}
		if(target.fetchEffect(ID())!=null)
		{
			mob.tell("This place is already under the peace moon.");
			return false;
		}
		for(int a=0;a<target.numEffects();a++)
		{
			Ability A=target.fetchEffect(a);
			if((A!=null)
			&&((A.classificationCode()&Ability.ALL_DOMAINS)==Ability.DOMAIN_MOONALTERING))
			{
				mob.tell("The moon is already under "+A.name()+", and can not be changed until this magic is gone.");
				return false;
			}
		}


		// the invoke method for spells receives as
		// parameters the invoker, and the REMAINING
		// command line parameters, divided into words,
		// and added as String objects to a vector.
		if(!super.invoke(mob,commands,givenTarget,auto,asLevel))
			return false;
		boolean success=proficiencyCheck(mob,0,auto);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.
			invoker=mob;
			CMMsg msg=CMClass.getMsg(mob,target,this,verbalCastCode(mob,target,auto),auto?"":"^S<S-NAME> chant(s) to the sky.^?");
			if(mob.location().okMessage(mob,msg))
			{
				mob.location().send(mob,msg);
				if(msg.value()<=0)
				{
					mob.location().showHappens(CMMsg.MSG_OK_VISUAL,"The Peace Moon Rises!");
					beneficialAffect(mob,target,asLevel,0);
				}
			}
		}
		else
			return maliciousFizzle(mob,target,"<S-NAME> chant(s) to the sky, but the magic fades.");
		// return whether it worked
		return success;
	}
}
