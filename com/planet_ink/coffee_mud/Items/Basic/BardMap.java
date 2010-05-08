package com.planet_ink.coffee_mud.Items.Basic;
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
public class BardMap extends GenMap
{
	public String ID(){	return "BardMap";}
	public BardMap()
	{
		super();
		setName("a map");
		baseEnvStats.setWeight(0);
		setDisplayText("a map is rolled up here.");
		setDescription("");
		baseGoldValue=5;
		baseEnvStats().setLevel(3);
		setMaterial(RawMaterial.RESOURCE_PAPER);
		recoverEnvStats();
	}

	public void doMapArea()
	{
		//myMap=null;
	}

	public StringBuffer[][] getMyMappedRoom(int width)
	{
        StringBuffer[][] myMap=null;
		//if(myMap!=null)	return myMap;
		myMap=finishMapMaking(width);
		return myMap;
	}

	public Hashtable makeMapRooms(int width)
	{
		String newText=getMapArea();
		Vector mapAreas=CMParms.parseSemicolons(newText,true);
		Hashtable mapRooms=new Hashtable();
		for(int a=0;a<mapAreas.size();a++)
		{
			String area=(String)mapAreas.elementAt(a);
			Room room=CMLib.map().getRoom(area);
			if(room!=null)
			{
				MapRoom mr=new MapRoom();
				mr.r=room;
				mapRooms.put(room,mr);
			}
		}
		super.clearTheSkys(mapRooms);
		return mapRooms;
	}
}