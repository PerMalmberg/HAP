package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.FSM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM

class NoAction constructor(fsm: UserInteractionFSM) : UserInteractionState(fsm)