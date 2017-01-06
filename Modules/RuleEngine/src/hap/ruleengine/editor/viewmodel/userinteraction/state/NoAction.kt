package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.FSM

class NoAction constructor(fsm: FSM<UserInteractionState>) : UserInteractionState(fsm)