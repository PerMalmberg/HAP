package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.css.PalletStyle
import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.ComponentPallet
import hap.ruleengine.editor.viewmodel.event.DragComponentFromComponentPallet
import hap.ruleengine.editor.viewmodel.event.DragCompositeFromComponentPallet
import hap.ruleengine.editor.viewmodel.event.EndDragComponentFromPallet
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.parts.CompositeVM
import tornadofx.*


class ComponentPalletView : Fragment()
{

	val pallet: ComponentPallet by inject()

	override val root =
				scrollpane {
					isFitToHeight = true
					isFitToWidth = true
					squeezebox {
						addClass(PalletStyle.componentRowStyle)
						pallet.categories.forEach {
							fold(it.category) {
								vbox(spacing = 5) {
									it.components.forEach {
										stackpane {

											val vm = it
											this += find<ComponentView>(mapOf(ComponentView::vm to it))

											setOnDragDetected {
												this.startFullDrag()
												when (vm)
												{
													is CompositeVM -> fire(DragCompositeFromComponentPallet(vm.sourceFile))
													is ComponentVM -> fire(DragComponentFromComponentPallet(vm.componentType))
												}

												it.consume()
											}

											setOnMouseReleased {
												fire(EndDragComponentFromPallet)
											}

											setOnMouseEntered {
												addClass(PalletStyle.mouseOver)
											}

											setOnMouseExited {
												removeClass(PalletStyle.mouseOver)
											}
										}
									}
								}
							}
						}
					}
				}

}
