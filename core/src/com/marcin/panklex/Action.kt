package com.marcin.panklex

import com.marcin.panklex.screens.ScreenGame

class Action(var actionTitle : String, var actionDescription : String, val performAction : (ScreenGame) -> Unit)
{
    var isActionPossible = false
}
