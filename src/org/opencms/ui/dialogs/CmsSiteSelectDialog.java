/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.ui.dialogs;

import org.opencms.gwt.CmsCoreService;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.ui.A_CmsUI;
import org.opencms.ui.CmsVaadinUtils;
import org.opencms.ui.I_CmsDialogContext;
import org.opencms.ui.apps.Messages;
import org.opencms.ui.components.CmsBasicDialog;
import org.opencms.ui.components.CmsOkCancelActionHandler;
import org.opencms.util.CmsStringUtil;
import org.opencms.util.CmsUUID;

import java.util.Collections;

import org.apache.commons.logging.Log;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;

/**
 * The site select dialog.<p>
 */
public class CmsSiteSelectDialog extends CmsBasicDialog {

    /** Logger instance for this class. */
    static final Log LOG = CmsLog.getLog(CmsSiteSelectDialog.class);

    /** The project name property. */
    private static final String CAPTION_PROPERTY = "caption";

    /** The serial version id. */
    private static final long serialVersionUID = 4455901453008760434L;

    /** The cancel button. */
    private Button m_cancelButton;

    /** The dialog context. */
    private I_CmsDialogContext m_context;

    /** The site select. */
    private ComboBox m_siteComboBox;

    /**
     * Constructor.<p>
     *
     * @param context the dialog context
     */
    public CmsSiteSelectDialog(I_CmsDialogContext context) {
        m_context = context;
        setContent(initForm());
        m_cancelButton = createButtonCancel();
        m_cancelButton.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {

                cancel();
            }
        });
        addButton(m_cancelButton);

        setActionHandler(new CmsOkCancelActionHandler() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void cancel() {

                CmsSiteSelectDialog.this.cancel();
            }

            @Override
            protected void ok() {

                submit();
            }
        });
    }

    /**
     * Cancels the dialog action.<p>
     */
    void cancel() {

        m_context.finish(Collections.<CmsUUID> emptyList());
    }

    /**
     * Submits the dialog action.<p>
     */
    void submit() {

        String siteRoot = (String)m_siteComboBox.getValue();
        if (!m_context.getCms().getRequestContext().getSiteRoot().equals(siteRoot)) {
            A_CmsUI.get().changeSite(siteRoot);
            if (CmsStringUtil.isEmptyOrWhitespaceOnly(siteRoot) || OpenCms.getSiteManager().isSharedFolder(siteRoot)) {
                // switch to explorer view when selecting shared or root site

                Page.getCurrent().open(CmsCoreService.getFileExplorerLink(A_CmsUI.getCmsObject(), siteRoot), "_top");
                return;
            }
        } else {
            siteRoot = null;
        }
        m_context.finish(null, siteRoot);
    }

    /**
     * Initializes the form component.<p>
     *
     * @return the form component
     */
    private FormLayout initForm() {

        FormLayout form = new FormLayout();
        form.setWidth("100%");

        IndexedContainer sites = CmsVaadinUtils.getAvailableSitesContainer(m_context.getCms(), CAPTION_PROPERTY);
        m_siteComboBox = prepareComboBox(sites, org.opencms.workplace.Messages.GUI_LABEL_SITE_0);
        m_siteComboBox.select(m_context.getCms().getRequestContext().getSiteRoot());
        form.addComponent(m_siteComboBox);
        ValueChangeListener changeListener = new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {

                submit();
            }
        };
        m_siteComboBox.addValueChangeListener(changeListener);
        return form;
    }

    /**
     * Prepares a combo box.<p>
     *
     * @param container the indexed item container
     * @param captionKey the caption message key
     *
     * @return the combo box
     */
    private ComboBox prepareComboBox(IndexedContainer container, String captionKey) {

        ComboBox result = new ComboBox(CmsVaadinUtils.getWpMessagesForCurrentLocale().key(captionKey), container);
        result.setTextInputAllowed(true);
        result.setNullSelectionAllowed(false);
        result.setWidth("100%");
        result.setInputPrompt(
            Messages.get().getBundle(UI.getCurrent().getLocale()).key(Messages.GUI_EXPLORER_CLICK_TO_EDIT_0));
        result.setItemCaptionPropertyId(CAPTION_PROPERTY);
        result.setFilteringMode(FilteringMode.CONTAINS);
        return result;
    }
}
