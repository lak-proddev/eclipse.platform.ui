/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui;

/**
 * Default implementation of INavigationLocation.
 *
 * @since 2.1
 */
public abstract class NavigationLocation implements INavigationLocation {

	private IWorkbenchPage page;

	private IEditorInput input;

	private String editorId;

	/**
	 * Constructs a NavigationLocation with its editor part.
	 *
	 * @param editorPart the part
	 */
	protected NavigationLocation(IEditorPart editorPart) {
		this.page = editorPart.getSite().getPage();
		this.editorId = editorPart.getSite().getId();
		this.input = editorPart.getEditorInput();
	}

	/**
	 * Returns the part that the receiver holds the location for.
	 *
	 * @return IEditorPart
	 */
	protected IEditorPart getEditorPart() {
		if (input == null) {
			return null;
		} else if (editorId == null) {
			return page.findEditor(input);
		} else {
			IEditorReference[] editorReferences = page.findEditors(input, editorId,
					IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
			if (editorReferences.length > 0) {
				return editorReferences[0].getEditor(false);
			}
			return null;
		}
	}

	@Override
	public Object getInput() {
		return input;
	}

	@Override
	public String getId() {
		return editorId;
	}

	@Override
	public String getText() {
		IEditorPart part = getEditorPart();
		if (part == null) {
			return ""; //$NON-NLS-1$
		}
		return part.getTitle();
	}

	@Override
	public void setInput(Object input) {
		this.input = (IEditorInput) input;
	}

	@Override
	public void setId(String id) {
		this.editorId = id;
	}

	/**
	 * May be extended by clients.
	 *
	 * @see org.eclipse.ui.INavigationLocation#dispose()
	 */
	@Override
	public void dispose() {
		releaseState();
	}

	/**
	 * May be extended by clients.
	 *
	 * @see org.eclipse.ui.INavigationLocation#releaseState()
	 */
	@Override
	public void releaseState() {
		input = null;
	}
}
