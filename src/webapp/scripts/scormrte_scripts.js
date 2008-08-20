// $Id: scormrte_scripts.js,v 1.1 2007/12/10 08:50:28 msegarra Exp $
/*
 * Copyright (c) 2007 Enginyeria La Salle. Universitat Ramon Llull.
 * This file is part of SCORM2004RTE.
 *
 * SCORM2004RTE is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details, currently published
 * at http://www.gnu.org/copyleft/gpl.html or in the gpl.txt in
 * the root folder of this distribution.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA 02110-1301, USA.
 */
 
function openWindow(url, title, options)
{
	var win = top.window.open(url, title, options);
	win.focus();
	
	return win;
}

/*
function sitehelp(whereto) 
{
	var umcthelp = openWindow(whereto,'umcthelpWindow','toolbar=yes,scrollbars=yes,resizable=yes,menubar=no,status=yes,directories=no,location=no,width=600,height=400')
}

function hideElement(hideMe)
{
	if (hideMe != 'none')
	{
		var menuItem = document.getElementById(hideMe);
		if(menuItem != null)
			menuItem.style.display = 'none';
	}
	return true;
}
*/