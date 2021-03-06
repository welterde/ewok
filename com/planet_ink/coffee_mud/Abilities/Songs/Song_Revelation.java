package com.planet_ink.coffee_mud.Abilities.Songs;
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
public class Song_Revelation extends Song
{

	public String ID() { return "Song_Revelation"; }
	public String name(){ return "Revelation";}
	public int abstractQuality(){ return Ability.QUALITY_OK_OTHERS;}
	
    public int castingQuality(MOB mob, Environmental target)
    {
        if(mob!=null)
        {
            if(target instanceof MOB)
            {
                Room R=((MOB)target).location();
                boolean found=false;
                if(R!=null)
                    for(int r=0;r<R.numInhabitants();r++)
                    {
                        MOB M=R.fetchInhabitant(r);
                        if((M!=null)&&(M!=mob)&&(M!=target)
                        &&(CMLib.flags().isHidden(M)||CMLib.flags().isInvisible(M)))
                        { found=true; break;}
                    }
                if(found)
                    return super.castingQuality(mob, target,Ability.QUALITY_BENEFICIAL_OTHERS);
            }
        }
        return super.castingQuality(mob,target);
    }

	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		if(invoker==null) return;
		affectableStats.setSensesMask(affectableStats.sensesMask()|EnvStats.CAN_SEE_DARK);
		affectableStats.setSensesMask(affectableStats.sensesMask()|EnvStats.CAN_SEE_INVISIBLE);
	}
}
