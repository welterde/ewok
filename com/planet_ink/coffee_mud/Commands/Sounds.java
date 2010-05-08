package com.planet_ink.coffee_mud.Commands;
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
public class Sounds extends StdCommand
{
	public Sounds(){}

	private String[] access={"SOUNDS","MSP"};
	public String[] getAccessWords(){return access;}
	public boolean execute(MOB mob, Vector commands, int metaFlags)
		throws java.io.IOException
	{
        if(!mob.isMonster())
        {
            if((!CMath.bset(mob.getBitmap(),MOB.ATT_SOUND))
            ||(!mob.session().clientTelnetMode(Session.TELNET_MSP)))
            {
                mob.session().changeTelnetMode(Session.TELNET_MSP,true);
                for(int i=0;((i<5)&&(!mob.session().clientTelnetMode(Session.TELNET_MSP)));i++)
                {
                    try{mob.session().prompt("",100);}catch(Exception e){}
                }
                if(mob.session().clientTelnetMode(Session.TELNET_MSP))
                {
                    mob.setBitmap(CMath.setb(mob.getBitmap(),MOB.ATT_SOUND));
                    mob.tell("MSP Sound/Music enabled.\n\r");
                }
                else
                    mob.tell("Your client does not appear to support MSP.");
            }
            else
            {
                mob.tell("MSP Sound/Music is already enabled.\n\r");
            }
        }
        return false;
	}
	
	public boolean canBeOrdered(){return true;}
	public boolean securityCheck(MOB mob){return super.securityCheck(mob)&&(!CMSecurity.isDisabled("MSP"));}
}