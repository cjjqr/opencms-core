/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/gwt/client/ui/tree/Attic/CmsTreeItem.java,v $
 * Date   : $Date: 2010/06/24 09:05:26 $
 * Version: $Revision: 1.16 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2009 Alkacon Software (http://www.alkacon.com)
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

package org.opencms.gwt.client.ui.tree;

import org.opencms.file.CmsResource;
import org.opencms.gwt.client.ui.CmsList;
import org.opencms.gwt.client.ui.CmsListItem;
import org.opencms.gwt.client.ui.CmsToggleButton;
import org.opencms.gwt.client.ui.css.I_CmsImageBundle;
import org.opencms.gwt.client.ui.css.I_CmsLayoutBundle;
import org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.I_CmsListTreeCss;
import org.opencms.gwt.client.ui.dnd.CmsDropEvent;
import org.opencms.gwt.client.ui.dnd.CmsDropPosition;
import org.opencms.gwt.client.ui.dnd.I_CmsDraggable;
import org.opencms.gwt.client.ui.dnd.I_CmsDropTarget;
import org.opencms.gwt.client.ui.input.CmsCheckBox;
import org.opencms.gwt.client.util.CmsDomUtil;
import org.opencms.gwt.client.util.CmsStyleVariable;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * List tree item implementation.<p>
 * 
 * Implemented as:
 * <pre>
 * &lt;li class='listTreeItem listTreeItem*state*'>
 *   &lt;span class='listTreeItemImage'>&lt;/span>
 *   &lt;div class='listTreeItemContent'>...*content*&lt;/div>
 *   &lt;ul class='listTreeItemChildren'>
 *      *children*
 *   &lt;/ul>
 * &lt;/li>
 * </pre>
 * 
 * Where state can be <code>opened</code>, <code>closed</code> or <code>leaf</code>.<p>
 * 
 * @author Georg Westenberger
 * @author Michael Moossen
 * 
 * @version $Revision: 1.16 $ 
 * 
 * @since 8.0.0
 */
public class CmsTreeItem extends CmsListItem {

    /** The duration of the animations. */
    public static final int ANIMATION_DURATION = 200;

    /** The CSS bundle used for this widget. */
    private static final I_CmsListTreeCss CSS = I_CmsLayoutBundle.INSTANCE.listTreeCss();

    /** The width of the opener. */
    private static final int OPENER_WIDTH = 16;

    /** The children list. */
    protected CmsList<CmsTreeItem> m_children;

    /** The element showing the open/close icon. */
    protected CmsToggleButton m_opener;

    /** Timer to open item while dragging and hovering. */
    protected Timer m_timer;

    /** Flag to indicate if drag'n drop is enabled. 3-states: if <code>null</code> the tree decides. */
    private Boolean m_dropEnabled;

    /** The style variable controlling this tree item's leaf/non-leaf state. */
    private CmsStyleVariable m_leafStyleVar;

    /** Flag to indicate if open or closed. */
    private boolean m_open;

    /** The item parent. */
    private CmsTreeItem m_parentItem;

    /** The style variable controlling this tree item's open/closed state. */
    private CmsStyleVariable m_styleVar;

    /** The tree reference. */
    private CmsTree<CmsTreeItem> m_tree;

    /**
     * Creates a new list tree item containing a main widget and a check box.<p>
     * 
     * @param showOpeners if true, show open/close icons
     * @param checkbox the check box 
     * @param mainWidget the main widget 
     */
    public CmsTreeItem(boolean showOpeners, CmsCheckBox checkbox, Widget mainWidget) {

        this(showOpeners);
        addMainWidget(mainWidget);
        addCheckBox(checkbox);
        initContent();
        if (!showOpeners) {
            hideOpeners();
        }
    }

    /**
     * Creates a new list tree item containing a main widget.<p>
     * 
     * @param showOpeners if true, show open/close icons 
     * @param mainWidget the main widget 
     */
    public CmsTreeItem(boolean showOpeners, Widget mainWidget) {

        this(showOpeners);
        addMainWidget(mainWidget);
        initContent();
        if (!showOpeners) {
            hideOpeners();
        }

    }

    /** 
     * Default constructor.<p>
     * 
     * @param showOpeners if true, the opener icons should be shown 
     */
    protected CmsTreeItem(boolean showOpeners) {

        super();
        m_styleVar = new CmsStyleVariable(this);
        m_leafStyleVar = new CmsStyleVariable(this);
        m_opener = createOpener();
        addDecoration(m_opener, showOpeners ? OPENER_WIDTH : 0, true);
        m_children = new CmsList<CmsTreeItem>();
        m_children.setStyleName(CSS.listTreeItemChildren());
        m_panel.add(m_children);
        onChangeChildren();
        m_open = true;
        setOpen(false);
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsListItem#add(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void add(Widget w) {

        throw new UnsupportedOperationException();
    }

    /**
     * Adds a child list item.<p>
     * 
     * @param item the child to add
     * 
     * @see org.opencms.gwt.client.ui.CmsList#addItem(org.opencms.gwt.client.ui.I_CmsListItem)
     */
    public void addChild(CmsTreeItem item) {

        m_children.addItem(item);
        adopt(item);
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsListItem#beforeDrop(org.opencms.gwt.client.ui.dnd.CmsDropEvent, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void beforeDrop(CmsDropEvent e, AsyncCallback<CmsDropEvent> callback) {

        if (!(e.getTarget() instanceof CmsTree<?>)) {

            if (e.getTarget() instanceof CmsList<?>) {
                // a list so let's check ids
                super.beforeDrop(e, callback);
                return;
            }

            // not a list so i can not check ids
            callback.onSuccess(e);
            return;
        }

        CmsTree<?> tree = (CmsTree<?>)e.getTarget();
        // get the drop position
        String destPath = e.getPosition().getInfo() + e.getPosition().getName() + "/";

        // if the path already exists
        Object item = tree.getItemByPath(destPath);
        if (item == null) {
            // the id does not exist, so everything is ok
            callback.onSuccess(e);
            return;
        }
        I_CmsDraggable draggable = e.getDraggable();
        if (draggable instanceof CmsTreeItem) {
            CmsTreeItem src = (CmsTreeItem)draggable;
            if ((src.getTree() == e.getTarget()) && destPath.equals(src.getPath())) {
                // just a position change
                callback.onSuccess(e);
                return;
            }
        }

        String newName = e.getPosition().getName();
        // TODO: use a nicer dialog, with blacklist for already used ids 
        while ((newName != null) && (tree.getItemByPath(e.getPosition().getInfo() + newName + "/") != null)) {
            // TODO: i18n
            newName = Window.prompt("duplicated name, please enter a new one", newName);
        }
        if (newName == null) {
            callback.onFailure(null);
        } else {
            e.getPosition().setName(newName);
            callback.onSuccess(new CmsDropEvent(CmsTreeItem.this, e.getTarget(), e.getPosition()));
        }
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsListItem#canDrop(org.opencms.gwt.client.ui.dnd.I_CmsDropTarget, org.opencms.gwt.client.ui.dnd.CmsDropPosition)
     */
    @Override
    public boolean canDrop(I_CmsDropTarget target, CmsDropPosition position) {

        if (target != getTree()) {
            // another target does not matter
            return true;
        }
        if (!getPath().equals(position.getInfo() + position.getName() + "/")) {
            return true;
        }
        int pPos = position.getPosition();
        int pos = CmsDomUtil.getPosition(getElement());
        // since this is hidden, we prevent dropping if the place holder is just before or just after this
        return (pPos < pos - 1) || (pPos > pos + 1);
    }

    /**
     * @see com.google.gwt.user.client.ui.HasWidgets#clear()
     */
    public void clear() {

        clearChildren();
    }

    /**
     * Removes all children.<p>
     * 
     * @see org.opencms.gwt.client.ui.CmsList#clearList()
     */
    public void clearChildren() {

        for (int i = getChildCount(); i > 0; i--) {
            removeChild(i - 1);
        }
    }

    /**
     * Returns the child tree item at the given position.<p>
     * 
     * @param index the position
     * 
     * @return the tree item
     * 
     * @see org.opencms.gwt.client.ui.CmsList#getItem(int)
     */
    public CmsTreeItem getChild(int index) {

        return m_children.getItem(index);
    }

    /**
     * Returns the tree item with the given id.<p>
     * 
     * @param itemId the id of the item to retrieve
     * 
     * @return the tree item
     * 
     * @see org.opencms.gwt.client.ui.CmsList#getItem(String)
     */
    public CmsTreeItem getChild(String itemId) {

        return m_children.getItem(itemId);
    }

    /**
     * Helper method which gets the number of children.<p>
     * 
     * @return the number of children
     * 
     * @see org.opencms.gwt.client.ui.CmsList#getWidgetCount()
     */
    public int getChildCount() {

        return m_children.getWidgetCount();
    }

    /**
     * Returns the given item position.<p>
     * 
     * @param item the item to get the position for
     * 
     * @return the item position
     */
    public int getItemPosition(CmsTreeItem item) {

        return m_children.getWidgetIndex(item);
    }

    /**
     * Returns the parent item.<p>
     *
     * @return the parent item
     */
    public CmsTreeItem getParentItem() {

        return m_parentItem;
    }

    /**
     * Returns the path of IDs for the this item.<p>
     * 
     * @return a path of IDs separated by slash
     */
    public String getPath() {

        StringBuffer path = new StringBuffer("/");
        CmsTreeItem current = this;
        while (current != null) {
            path.insert(0, current.getId()).insert(0, "/");
            current = current.getParentItem();
        }
        return path.toString();
    }

    /**
     * Gets the tree to which this tree item belongs, or null if it does not belong to a tree.<p>
     * 
     * @return a tree or <code>null</code>
     */
    public CmsTree<CmsTreeItem> getTree() {

        return m_tree;
    }

    /** 
     * Hides the open/close icons for this tree item and its descendants.<p>
     */
    public void hideOpeners() {

        addStyleName(CSS.listTreeItemNoOpeners());
    }

    /**
     * Inserts the given item at the given position.<p>
     * 
     * @param item the item to insert
     * @param position the position
     * 
     * @see org.opencms.gwt.client.ui.CmsList#insertItem(org.opencms.gwt.client.ui.I_CmsListItem, int)
     */
    public void insertChild(CmsTreeItem item, int position) {

        m_children.insert(item, position);
        adopt(item);
    }

    /**
     * Checks if dropping is enabled.<p>
     *
     * @return <code>true</code> if dropping is enabled
     */
    public boolean isDropEnabled() {

        if (m_dropEnabled != null) {
            return m_dropEnabled.booleanValue();
        }
        CmsTree<?> tree = getTree();
        if (tree == null) {
            return false;
        }
        return tree.isDropEnabled();
    }

    /**
     * Checks if the item is open or closed.<p>
     *
     * @return <code>true</code> if open
     */
    public boolean isOpen() {

        return m_open;
    }

    /**
     * Will be executed when the user drags something over this item.<p>
     * 
     * Will also check that drop over is allowed
     * 
     * @return <code>true</code> if drop over is allowed
     */
    public boolean onDragOverIn() {

        getListItemWidget().getContentPanel().addStyleName(I_CmsLayoutBundle.INSTANCE.listItemWidgetCss().itemActive());
        if (isOpen()) {
            return true;
        }
        m_timer = new Timer() {

            /**
             * @see com.google.gwt.user.client.Timer#run()
             */
            @Override
            public void run() {

                m_timer = null;
                setOpen(true);
            }
        };
        m_timer.schedule(1000);
        return true;
    }

    /**
     * Will be executed when the user stops dragging something over this item.<p>
     */
    public void onDragOverOut() {

        if (m_timer != null) {
            m_timer.cancel();
            m_timer = null;
        }
        getListItemWidget().getContentPanel().removeStyleName(
            I_CmsLayoutBundle.INSTANCE.listItemWidgetCss().itemActive());
    }

    /**
     * Removes an item from the list.<p>
     * 
     * @param item the item to remove
     * 
     * @return the removed item 
     * 
     * @see org.opencms.gwt.client.ui.CmsList#removeItem(org.opencms.gwt.client.ui.I_CmsListItem)
     */
    public CmsTreeItem removeChild(final CmsTreeItem item) {

        item.setParentItem(null);
        item.setTree(null);
        if ((m_tree != null) && m_tree.isAnimationEnabled()) {
            // could be null if already detached
            // animate
            (new Animation() {

                /**
                 * @see com.google.gwt.animation.client.Animation#onComplete()
                 */
                @Override
                protected void onComplete() {

                    super.onComplete();
                    m_children.removeItem(item);
                    onChangeChildren();
                }

                /**
                 * @see com.google.gwt.animation.client.Animation#onUpdate(double)
                 */
                @Override
                protected void onUpdate(double progress) {

                    item.getElement().getStyle().setOpacity(1 - progress);
                }
            }).run(ANIMATION_DURATION);
        } else {
            m_children.removeItem(item);
            onChangeChildren();
        }
        return item;
    }

    /**
     * Removes the item identified by the given index from the list.<p>
     * 
     * @param index the index of the item to remove
     * 
     * @return the removed item 
     * 
     * @see org.opencms.gwt.client.ui.CmsList#remove(int)
     */
    public CmsTreeItem removeChild(int index) {

        return removeChild(m_children.getItem(index));
    }

    /**
     * Removes an item from the list.<p>
     * 
     * @param itemId the id of the item to remove
     * 
     * @return the removed item
     * 
     * @see org.opencms.gwt.client.ui.CmsList#removeItem(String)
     */
    public CmsTreeItem removeChild(String itemId) {

        return removeChild(m_children.getItem(itemId));
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsListItem#resetPlaceHolder()
     */
    @Override
    public CmsDropPosition resetPlaceHolder() {

        m_placeholder = getPlaceHolder(getTree());
        getElement().getParentElement().insertAfter(m_placeholder, getElement());
        getTree().setPlaceholder(m_placeholder);
        return new CmsDropPosition(
            getId(),
            CmsDomUtil.getPosition(m_placeholder),
            CmsResource.getParentFolder(getPath()),
            m_placeholder);
    }

    /**
     * Enables/disables dropping.<p>
     *
     * @param enabled <code>true</code> to enable, or <code>false</code> to disable
     */
    public void setDropEnabled(boolean enabled) {

        if ((m_dropEnabled != null) && (m_dropEnabled.booleanValue() == enabled)) {
            return;
        }
        m_dropEnabled = Boolean.valueOf(enabled);
    }

    /**
     * Opens or closes this tree item (i.e. shows or hides its descendants).<p>
     * 
     * @param open if true, open the tree item, else close it
     */
    public void setOpen(boolean open) {

        if (m_open == open) {
            return;
        }
        m_open = open;

        m_styleVar.setValue(open ? CSS.listTreeItemOpen() : CSS.listTreeItemClosed());
        m_opener.setDown(open);
        if (open) {
            fireOpen();
        }
    }

    /**
     * Sets the parent item.<p>
     *
     * @param parentItem the parent item to set
     */
    public void setParentItem(CmsTreeItem parentItem) {

        m_parentItem = parentItem;
    }

    /**
     * Inserts the place holder in the right position given the dragging coordinates.<p>
     * 
     * @param x the horizontal position
     * @param y the vertical position
     * @param event the current tentative drop event
     * 
     * @return the place holder position
     * 
     * @see org.opencms.gwt.client.ui.dnd.I_CmsDropTarget#setPlaceholder(int, int, CmsDropEvent)
     */
    public CmsDropPosition setPlaceholder(int x, int y, CmsDropEvent event) {

        Element itemElement = getListItemWidget().getElement();
        // check if the mouse pointer is within the width of the element 
        int left = CmsDomUtil.getRelativeX(x, itemElement);
        if ((left <= 0) || (left >= itemElement.getOffsetWidth())) {
            // out of widget
            //CmsDebugLog.getInstance().printLine(getPath() + ": x out");
            return null;
        }

        // check if the mouse pointer is within the height of the element 
        int top = CmsDomUtil.getRelativeY(y, itemElement);
        int height = itemElement.getOffsetHeight();
        if (top <= 0) { // (top >= height) is handled later
            // out of widget
            //CmsDebugLog.getInstance().printLine(getPath() + ": y out");
            return null;
        }

        int index;
        String path;
        boolean isParentDndEnabled;
        if (getParentItem() == null) {
            index = getTree().getItemPosition(this);
            path = "/";
            isParentDndEnabled = getTree().isDropEnabled();
        } else {
            index = getParentItem().getItemPosition(this);
            path = getParentItem().getPath();
            isParentDndEnabled = getParentItem().isDropEnabled();
        }

        Element parentElement = getElement().getParentElement();
        I_CmsDraggable draggable = event.getDraggable();
        boolean checkPos = ((event.getTarget() == this.getTree()) && (event.getPosition() != null));
        if (draggable == this) {
            // this case occurs when start dragging
            if (checkPos && (event.getPosition().getPosition() == index) && path.equals(event.getPosition().getInfo())) {
                // nothing has changed
                return null;
            }
            // insert place holder at the parent before the current item
            Element newPlaceholder = draggable.getPlaceHolder(getTree());
            getTree().setPlaceholder(newPlaceholder);
            parentElement.insertBefore(newPlaceholder, getElement());
            CmsDropPosition pos = new CmsDropPosition(null, index, path, newPlaceholder);
            //CmsDebugLog.getInstance().printLine(getPath() + ": 1: " + pos);
            return pos;
        } else if (top < height / 3) {
            // the mouse pointer is within the top section of the widget

            // In this case try to drop on the parent
            if (!isParentDndEnabled) {
                // we are not allowed to drop here
                //CmsDebugLog.getInstance().printLine(getPath() + ": parent not enabled 2");
                return null;
            }
            if (checkPos && (event.getPosition().getPosition() == index) && path.equals(event.getPosition().getInfo())) {
                // nothing has changed
                return null;
            }
            // insert place holder at the parent before the current item
            Element newPlaceholder = draggable.getPlaceHolder(getTree());
            getTree().setPlaceholder(newPlaceholder);
            parentElement.insertBefore(newPlaceholder, getElement());
            CmsDropPosition pos = new CmsDropPosition(null, index, path, newPlaceholder);
            //CmsDebugLog.getInstance().printLine(getPath() + ": 2: " + pos);
            return pos;
        } else if ((top > 2 * height / 3) && (top < height)) {
            // the mouse pointer is within the bottom section of the widget
            if ((getChildCount() == 0) || !isOpen()) {
                // if the item does not have children or is closed use the same level

                // In this case try to drop on the parent
                if (!isParentDndEnabled) {
                    // we are not allowed to drop here
                    //CmsDebugLog.getInstance().printLine(getPath() + ": parent not enabled 3");
                    return null;
                }
                if (checkPos
                    && (event.getPosition().getPosition() == index + 1)
                    && path.equals(event.getPosition().getInfo())) {
                    // nothing has changed
                    return null;
                }
                // insert place holder at the parent after the current item
                Element newPlaceholder = draggable.getPlaceHolder(getTree());
                getTree().setPlaceholder(newPlaceholder);
                parentElement.insertAfter(newPlaceholder, getElement());
                CmsDropPosition pos = new CmsDropPosition(null, index + 1, path, newPlaceholder);
                //CmsDebugLog.getInstance().printLine(getPath() + ": 3: " + pos);
                return pos;
            } else {
                // but if it has children and they are visible use them

                // In this case try to drop on this item
                if (!isDropEnabled()) {
                    // we are not allowed to drop here
                    //CmsDebugLog.getInstance().printLine(getPath() + ": this not enabled 1");
                    return null;
                }
                if (checkPos
                    && (event.getPosition().getPosition() == 0)
                    && getPath().equals(event.getPosition().getInfo())) {
                    // nothing has changed
                    return null;
                }
                Element newPlaceholder = draggable.getPlaceHolder(getTree());
                getTree().setPlaceholder(newPlaceholder);
                m_children.getElement().insertBefore(newPlaceholder, m_children.getElement().getFirstChild());
                CmsDropPosition pos = new CmsDropPosition(null, 0, getPath(), newPlaceholder);
                //CmsDebugLog.getInstance().printLine(getPath() + ": 4: " + pos);
                return pos;
            }
        } else if (top < height) {
            // the mouse pointer is within the middle section of the element

            // In this case try to drop on this item
            if (!isDropEnabled()) {
                // we are not allowed to drop here
                //CmsDebugLog.getInstance().printLine(getPath() + ": parent not enabled 2");
                return null;
            }
            if (checkPos
                && (event.getPosition().getPosition() == getChildCount())
                && getPath().equals(event.getPosition().getInfo())) {
                // nothing has changed
                return null;
            }
            getTree().setPlaceholder(this); // this will activate hover effect
            CmsDropPosition pos = new CmsDropPosition(null, getChildCount(), getPath(), getElement());
            //CmsDebugLog.getInstance().printLine(getPath() + ": 5: " + pos);
            return pos;
        } else if (isOpen()) {
            // the mouse pointer is on children
            for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
                CmsTreeItem child = getChild(childIndex);
                Element childElement = child.getElement();

                String positioning = childElement.getStyle().getPosition();
                if ((positioning.equals(Position.ABSOLUTE.getCssName()) || positioning.equals(Position.FIXED.getCssName()))
                    || !child.isVisible()) {
                    // only take visible and not 'position:absolute' elements into account
                    //CmsDebugLog.getInstance().printLine(child.getPath() + ": not visible");
                    continue;
                }

                // check if the mouse pointer is within the width of the element 
                int childLeft = CmsDomUtil.getRelativeX(x, childElement);
                if ((childLeft <= 0) || (childLeft >= childElement.getOffsetWidth())) {
                    //CmsDebugLog.getInstance().printLine(child.getPath() + ": x out 2");
                    continue;
                }

                // check if the mouse pointer is within the height of the element 
                int childTop = CmsDomUtil.getRelativeY(y, childElement);
                int childHeight = childElement.getOffsetHeight();
                if ((childTop <= 0) || (childTop >= childHeight)) {
                    //CmsDebugLog.getInstance().printLine(child.getPath() + ": y out 2");
                    continue;
                }

                return child.setPlaceholder(x, y, event);
            }
            //CmsDebugLog.getInstance().printLine(getPath() + ": no match 1");
            return null;
        }
        //CmsDebugLog.getInstance().printLine(getPath() + ": no match 2");
        return null;
    }

    /**
     * Sets the tree to which this tree item belongs.<p>
     * 
     * This is automatically called when this tree item or one of its ancestors is inserted into a tree.<p>
     * 
     * @param tree the tree into which the item has been inserted
     */
    public void setTree(CmsTree<CmsTreeItem> tree) {

        m_tree = tree;
        for (Widget widget : m_children) {
            if (widget instanceof CmsTreeItem) {
                ((CmsTreeItem)widget).setTree(tree);
            }
        }
    }

    /**
     * Adopts the given item.<p>
     * 
     * @param item the item to adopt
     */
    protected void adopt(final CmsTreeItem item) {

        item.setParentItem(this);
        item.setTree(m_tree);
        onChangeChildren();
        if ((m_tree != null) && m_tree.isAnimationEnabled()) {
            // could be null if not yet attached
            item.getElement().getStyle().setOpacity(0);
            // animate
            (new Animation() {

                /**
                 * @see com.google.gwt.animation.client.Animation#onUpdate(double)
                 */
                @Override
                protected void onUpdate(double progress) {

                    item.getElement().getStyle().setOpacity(progress);
                }
            }).run(ANIMATION_DURATION);
        }
    }

    /**
     * Creates the button for opening/closing this item.<p>
     * 
     * @return a button
     */
    protected CmsToggleButton createOpener() {

        final CmsToggleButton opener = new CmsToggleButton();
        opener.setStyleName(CSS.listTreeItemHandler());

        opener.setUpFace("", CSS.plus());
        opener.setDownFace("", CSS.minus());
        opener.addClickHandler(new ClickHandler() {

            /**
             * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
             */
            public void onClick(ClickEvent e) {

                setOpen(opener.isDown());
            }
        });
        return opener;
    }

    /** 
     * Fires the open event on the tree.<p> 
     */
    protected void fireOpen() {

        if (m_tree != null) {
            m_tree.fireOpen(this);
        }
    }

    /**
     * The '-' image.<p>
     * 
     * @return the minus image 
     */
    protected Image getMinusImage() {

        return new Image(I_CmsImageBundle.INSTANCE.minus());
    }

    /**
     * The '+' image.<p>
     * 
     * @return the plus image 
     */
    protected Image getPlusImage() {

        return new Image(I_CmsImageBundle.INSTANCE.plus());
    }

    /**
     * Helper method which is called when the list of children changes.<p> 
     */
    protected void onChangeChildren() {

        int count = getChildCount();
        m_leafStyleVar.setValue(count == 0 ? CSS.listTreeItemLeaf() : CSS.listTreeItemInternal());
    }
}
