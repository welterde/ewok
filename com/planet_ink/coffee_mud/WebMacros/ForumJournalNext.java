package com.planet_ink.coffee_mud.WebMacros;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.JournalsLibrary.CommandJournal;
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
public class ForumJournalNext extends StdWebMacro
{
	public String name(){return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);}

	public static MOB guestM = null;
	
	public String runMacro(ExternalHTTPRequests httpReq, String parm)
	{
		Hashtable parms=parseParms(parm);
		String last=httpReq.getRequestParameter("JOURNAL");
		if(parms.containsKey("RESET"))
		{	
			if(last!=null) httpReq.removeRequestParameter("JOURNAL");
			httpReq.getRequestObjects().remove("JOURNALLIST");
			return "";
		}
		MOB M = Authenticate.getAuthenticatedMob(httpReq);
		if(M==null)
		{
			if(guestM==null)
			{
				guestM=CMClass.getMOB("StdMOB");
				guestM.baseEnvStats().setLevel(0);
				guestM.envStats().setLevel(0);
				guestM.setName("guest");
			}
			M=guestM;
		}
		
		Vector journals=(Vector)httpReq.getRequestObjects().get("JOURNALLIST");
		if(journals==null)
		{
			journals=new Vector();
			for(Enumeration e=CMLib.journals().forumJournals();e.hasMoreElements();)
			{
				JournalsLibrary.ForumJournal CJ=(JournalsLibrary.ForumJournal)e.nextElement();
				if((!journals.contains(CJ.NAME().toUpperCase()))
				&&(CMLib.masking().maskCheck(CJ.readMask(), M, true)))
					journals.add(CJ.NAME());
			}
			httpReq.getRequestObjects().put("JOURNALLIST",journals);
		}
		String lastID="";
		HashSet<String> H=CMLib.journals().getArchonJournalNames();
		for(int j=0;j<journals.size();j++)
		{
			String B=(String)journals.elementAt(j);
			if((H.contains(B.toUpperCase().trim()))&&((M==null)||(!CMSecurity.isASysOp(M))))
			    continue;
			if((last==null)||((last.length()>0)&&(last.equals(lastID))&&(!B.equals(lastID))))
			{
				httpReq.addRequestParameters("JOURNAL",B);
				return "";
			}
			lastID=B;
		}
		httpReq.addRequestParameters("JOURNAL","");
		if(parms.containsKey("EMPTYOK"))
			return "<!--EMPTY-->";
		return " @break@";
	}

}
