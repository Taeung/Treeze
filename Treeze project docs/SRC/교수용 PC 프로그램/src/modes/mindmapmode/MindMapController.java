/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/* $Id: MindMapController.java,v 1.35.14.21.2.81 2010/10/07 21:19:51 christianfoltin Exp $ */

package freemind.modes.mindmapmode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import sun.security.jgss.LoginConfigImpl;

import freemind.common.XmlBindingTools;
import freemind.controller.MenuBar;
import freemind.controller.MenuItemEnabledListener;
import freemind.controller.MindMapNodesSelection;
import freemind.controller.StructuredMenuHolder;
import freemind.controller.actions.generated.instance.EditNoteToNodeAction;
import freemind.controller.actions.generated.instance.MenuActionBase;
import freemind.controller.actions.generated.instance.MenuCategoryBase;
import freemind.controller.actions.generated.instance.MenuCheckedAction;
import freemind.controller.actions.generated.instance.MenuRadioAction;
import freemind.controller.actions.generated.instance.MenuSeparator;
import freemind.controller.actions.generated.instance.MenuStructure;
import freemind.controller.actions.generated.instance.MenuSubmenu;
import freemind.controller.actions.generated.instance.Pattern;
import freemind.controller.actions.generated.instance.PatternEdgeColor;
import freemind.controller.actions.generated.instance.PatternEdgeStyle;
import freemind.controller.actions.generated.instance.PatternEdgeWidth;
import freemind.controller.actions.generated.instance.PatternIcon;
import freemind.controller.actions.generated.instance.PatternNodeBackgroundColor;
import freemind.controller.actions.generated.instance.PatternNodeColor;
import freemind.controller.actions.generated.instance.PatternNodeFontBold;
import freemind.controller.actions.generated.instance.PatternNodeFontItalic;
import freemind.controller.actions.generated.instance.PatternNodeFontName;
import freemind.controller.actions.generated.instance.PatternNodeFontSize;
import freemind.controller.actions.generated.instance.PatternNodeStyle;
import freemind.controller.actions.generated.instance.PatternNodeText;
import freemind.controller.actions.generated.instance.WindowConfigurationStorage;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.extensions.HookFactory;
import freemind.extensions.HookRegistration;
import freemind.extensions.ModeControllerHook;
import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import freemind.extensions.UndoEventReceiver;
import freemind.extensions.HookFactory.RegistrationContainer;
import freemind.main.ExampleFileFilter;
import freemind.main.FixedHTMLWriter;
import freemind.main.HtmlTools;
import freemind.main.LoginFrame;
import freemind.main.Resources;
import freemind.main.Tools;
import freemind.main.XMLElement;
import freemind.modes.ControllerAdapter;
import freemind.modes.EdgeAdapter;
import freemind.modes.MapAdapter;
import freemind.modes.MindIcon;
import freemind.modes.MindMap;
import freemind.modes.MindMapArrowLink;
import freemind.modes.MindMapLink;
import freemind.modes.MindMapNode;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.modes.NodeAdapter;
import freemind.modes.NodeDownAction;
import freemind.modes.StylePatternFactory;
import freemind.modes.attributes.Attribute;
import freemind.modes.attributes.AttributeController;
import freemind.modes.attributes.NodeAttributeTableModel;
import freemind.modes.common.CommonNodeKeyListener;
import freemind.modes.common.GotoLinkNodeAction;
import freemind.modes.common.CommonNodeKeyListener.EditHandler;
import freemind.modes.common.actions.FindAction;
import freemind.modes.common.actions.NewMapAction;
import freemind.modes.common.actions.FindAction.FindNextAction;
import freemind.modes.common.listeners.CommonNodeMouseMotionListener;
import freemind.modes.common.listeners.MindMapMouseWheelEventHandler;
import freemind.modes.mindmapmode.actions.AddArrowLinkAction;
import freemind.modes.mindmapmode.actions.AddLocalLinkAction;
import freemind.modes.mindmapmode.actions.ApplyPatternAction;
import freemind.modes.mindmapmode.actions.BoldAction;
import freemind.modes.mindmapmode.actions.ChangeArrowLinkEndPoints;
import freemind.modes.mindmapmode.actions.ChangeArrowsInArrowLinkAction;
import freemind.modes.mindmapmode.actions.CloudAction;
import freemind.modes.mindmapmode.actions.ColorArrowLinkAction;
import freemind.modes.mindmapmode.actions.CompoundActionHandler;
import freemind.modes.mindmapmode.actions.CopyAction;
import freemind.modes.mindmapmode.actions.CopySingleAction;
import freemind.modes.mindmapmode.actions.CutAction;
import freemind.modes.mindmapmode.actions.DeleteChildAction;
import freemind.modes.mindmapmode.actions.EdgeColorAction;
import freemind.modes.mindmapmode.actions.EdgeStyleAction;
import freemind.modes.mindmapmode.actions.EdgeWidthAction;
import freemind.modes.mindmapmode.actions.EditAction;
import freemind.modes.mindmapmode.actions.ExportBranchAction;
import freemind.modes.mindmapmode.actions.FontFamilyAction;
import freemind.modes.mindmapmode.actions.FontSizeAction;
import freemind.modes.mindmapmode.actions.HookAction;
import freemind.modes.mindmapmode.actions.IconAction;
import freemind.modes.mindmapmode.actions.ImportExplorerFavoritesAction;
import freemind.modes.mindmapmode.actions.ImportFolderStructureAction;
import freemind.modes.mindmapmode.actions.ItalicAction;
import freemind.modes.mindmapmode.actions.JoinNodesAction;
import freemind.modes.mindmapmode.actions.MindMapActions;
import freemind.modes.mindmapmode.actions.MindMapControllerHookAction;
import freemind.modes.mindmapmode.actions.ModeControllerActionHandler;
import freemind.modes.mindmapmode.actions.MoveNodeAction;
import freemind.modes.mindmapmode.actions.NewChildAction;
import freemind.modes.mindmapmode.actions.NewPreviousSiblingAction;
import freemind.modes.mindmapmode.actions.NewSiblingAction;
import freemind.modes.mindmapmode.actions.NodeBackgroundColorAction;
import freemind.modes.mindmapmode.actions.NodeColorAction;
import freemind.modes.mindmapmode.actions.NodeColorBlendAction;
import freemind.modes.mindmapmode.actions.NodeGeneralAction;
import freemind.modes.mindmapmode.actions.NodeHookAction;
import freemind.modes.mindmapmode.actions.NodeStyleAction;
import freemind.modes.mindmapmode.actions.NodeUpAction;
import freemind.modes.mindmapmode.actions.OpenAction;
import freemind.modes.mindmapmode.actions.PasteAction;
import freemind.modes.mindmapmode.actions.RedoAction;
import freemind.modes.mindmapmode.actions.RemoveAllIconsAction;
import freemind.modes.mindmapmode.actions.RemoveArrowLinkAction;
import freemind.modes.mindmapmode.actions.RemoveIconAction;
import freemind.modes.mindmapmode.actions.RevertAction;
import freemind.modes.mindmapmode.actions.SelectAllAction;
import freemind.modes.mindmapmode.actions.SelectBranchAction;
import freemind.modes.mindmapmode.actions.SetLinkByTextFieldAction;
import freemind.modes.mindmapmode.actions.SingleNodeOperation;
import freemind.modes.mindmapmode.actions.ToggleChildrenFoldedAction;
import freemind.modes.mindmapmode.actions.ToggleFoldedAction;
import freemind.modes.mindmapmode.actions.UnderlinedAction;
import freemind.modes.mindmapmode.actions.UndoAction;
import freemind.modes.mindmapmode.actions.UsePlainTextAction;
import freemind.modes.mindmapmode.actions.UseRichFormattingAction;
import freemind.modes.mindmapmode.actions.NodeBackgroundColorAction.RemoveNodeBackgroundColorAction;
import freemind.modes.mindmapmode.actions.xml.ActionFactory;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.NodeHookUndoableContentActor;
import freemind.modes.mindmapmode.actions.xml.UndoActionHandler;
import freemind.modes.mindmapmode.attributeactors.AssignAttributeDialog;
import freemind.modes.mindmapmode.attributeactors.MindMapModeAttributeController;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;
import freemind.modes.mindmapmode.listeners.MindMapMouseMotionManager;
import freemind.modes.mindmapmode.listeners.MindMapNodeDropListener;
import freemind.modes.mindmapmode.listeners.MindMapNodeMotionListener;
import freemind.view.MapModule;
import freemind.view.mindmapview.MainView;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.attributeview.AttributePopupMenu;





public class MindMapController extends ControllerAdapter implements MindMapActions{


    private static final String ACCESSORIES_PLUGINS_NODE_NOTE = "accessories.plugins.NodeNote";

	protected class AssignAttributesAction extends AbstractAction {
        public AssignAttributesAction() {
            super(getText("attributes_assign_dialog"));
        }
        public void actionPerformed(ActionEvent e) {
             if(assignAttributeDialog == null){
                assignAttributeDialog = new AssignAttributeDialog(getView());
            }
            assignAttributeDialog.show();
        }
     }
	public interface MindMapControllerPlugin {
		
	}

	private static final String RESOURCE_UNFOLD_ON_PASTE = "unfold_on_paste";
	private static Logger logger;
	// for MouseEventHandlers
	private HashSet mRegisteredMouseWheelEventHandler = new HashSet();

    private ActionFactory actionFactory;
	private Vector hookActions;
	//    Mode mode;
    private MindMapPopupMenu popupmenu;
    //private JToolBar toolbar;
    private MindMapToolBar toolbar;
    private boolean addAsChildMode = false;
    private Clipboard clipboard;
    private Clipboard selection;
    
    private HookFactory nodeHookFactory;

    /**
     * This handler evaluates the compound xml actions. Don't delete it!
     */
    private CompoundActionHandler compound = null;

   public ApplyPatternAction patterns[] = new ApplyPatternAction[0]; // Make sure it is initialized
   public Action newMap = new NewMapAction(this);
   public Action open = new OpenAction(this);
   public Action save = new SaveAction(this);
   public Action saveAs = new SaveAsAction(this);
   public Action exportToHTML = new ExportToHTMLAction(this);
   public Action exportBranchToHTML = new ExportBranchToHTMLAction(this);

   public Action editLong = new EditLongAction();
   public Action editAttributes = new EditAttributesAction();
   protected  AssignAttributeDialog assignAttributeDialog = null;
   public Action assignAttributes = new AssignAttributesAction();
   public Action newSibling = new NewSiblingAction(this);
   public Action newPreviousSibling = new NewPreviousSiblingAction(this);
   public Action setLinkByFileChooser = new SetLinkByFileChooserAction();
   public Action setImageByFileChooser = new SetImageByFileChooserAction();
   public Action followLink = new FollowLinkAction();
   public Action exportBranch = new ExportBranchAction(this);
   public Action importBranch = new ImportBranchAction();
   public Action importLinkedBranch = new ImportLinkedBranchAction();
   public Action importLinkedBranchWithoutRoot = new ImportLinkedBranchWithoutRootAction();

   public Action showAttributeManagerAction = null;
   public Action propertyAction = null;


    public Action increaseNodeFont = new NodeGeneralAction (this, "increase_node_font_size", null,
       new SingleNodeOperation() { public void apply(MindMapMapModel map, MindMapNodeModel node) {
           increaseFontSize(node, 1);
    }});
    public Action decreaseNodeFont = new NodeGeneralAction (this, "decrease_node_font_size", null,
       new SingleNodeOperation() { public void apply(MindMapMapModel map, MindMapNodeModel node) {
           increaseFontSize(node, -1);
    }});

    public UndoAction undo=null;
    public RedoAction redo=null;
    public CopyAction copy = null;
    public Action copySingle = null;
    public CutAction cut = null;
    public PasteAction paste = null;
    public BoldAction bold = null;
    public ItalicAction italic = null;
    public UnderlinedAction underlined = null;
    public FontSizeAction fontSize = null;
    public FontFamilyAction fontFamily = null;
    public NodeColorAction nodeColor = null;
    public EditAction edit = null;
    public NewChildAction newChild = null;
    public DeleteChildAction deleteChild = null;
    public ToggleFoldedAction toggleFolded = null;
    public ToggleChildrenFoldedAction toggleChildrenFolded = null;
    public UseRichFormattingAction useRichFormatting = null;
    public UsePlainTextAction usePlainText = null;
    public NodeUpAction nodeUp = null;
    public NodeDownAction nodeDown = null;
    public EdgeColorAction edgeColor = null;
    public EdgeWidthAction EdgeWidth_WIDTH_PARENT = null;
    public EdgeWidthAction EdgeWidth_WIDTH_THIN = null;
    public EdgeWidthAction EdgeWidth_1 = null;
    public EdgeWidthAction EdgeWidth_2 = null;
    public EdgeWidthAction EdgeWidth_4 = null;
    public EdgeWidthAction EdgeWidth_8 = null;
    public EdgeWidthAction edgeWidths[] = null;
    public EdgeStyleAction EdgeStyle_linear = null;
    public EdgeStyleAction EdgeStyle_bezier = null;
    public EdgeStyleAction EdgeStyle_sharp_linear = null;
    public EdgeStyleAction EdgeStyle_sharp_bezier = null;
    public EdgeStyleAction edgeStyles[] = null;
    public NodeColorBlendAction nodeColorBlend = null;
    public NodeStyleAction fork = null;
    public NodeStyleAction bubble = null;
    public CloudAction cloud = null;
    public freemind.modes.mindmapmode.actions.CloudColorAction cloudColor = null;
    public AddArrowLinkAction addArrowLinkAction = null;
    public RemoveArrowLinkAction removeArrowLinkAction = null;
    public ColorArrowLinkAction colorArrowLinkAction = null;
    public ChangeArrowsInArrowLinkAction changeArrowsInArrowLinkAction = null;
    public NodeBackgroundColorAction nodeBackgroundColor = null;
    public RemoveNodeBackgroundColorAction removeNodeBackgroundColor = null;

    public IconAction unknownIconAction = null;
    public RemoveIconAction removeLastIconAction = null;
    public RemoveAllIconsAction removeAllIconsAction = null;
    public SetLinkByTextFieldAction setLinkByTextField = null;
    public AddLocalLinkAction addLocalLinkAction = null;
    public GotoLinkNodeAction gotoLinkNodeAction = null;
    public JoinNodesAction joinNodes = null;
    public MoveNodeAction moveNodeAction = null;
    public ImportExplorerFavoritesAction importExplorerFavorites = null;
    public ImportFolderStructureAction importFolderStructure = null;
    public ChangeArrowLinkEndPoints changeArrowLinkEndPoints = null;

    public FindAction find=null;
    public FindNextAction findNext=null;
    public NodeHookAction nodeHookAction = null;
    public RevertAction revertAction = null;
    public SelectBranchAction selectBranchAction = null;
    public SelectAllAction selectAllAction = null;
    //dewlit
    private String childName = "";
    //양식 없는거 노드 이름 때문에
    //dewlit
    
    //dewlit
	static int FIRST = 1;
	private int lectureId;
	private String lectureTitle;
	//dewlit
	
	public String getLectureTitle() {
		return lectureTitle;
	}

	public void setLectureTitle(String lectureTitle) {
		this.lectureTitle = lectureTitle;
	}

	public int getLectureId() {
		return lectureId;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	// Extension Actions
    public Vector iconActions = new Vector(); //fc
    public NodeHookUndoableContentActor undoableHookContentActor = null;

    FileFilter filefilter = new MindMapFilter();

    private MenuStructure mMenuStructure;
    private List mRegistrations;
	private List mPatternsList = new Vector();
	
	public void dd(){
		System.out.println("dd");
	}
	
    public MindMapController(Mode mode) {
	super(mode);
	if(logger == null) {
		logger = getFrame().getLogger(this.getClass().getName());
	}
	
	if(FIRST == 1){ // 클래스를 두번 만듬, 한번만 호출하게
		new LoginFrame(this);
		FIRST++;
	}
    // create action factory:
    actionFactory = new ActionFactory(getController());
    // create compound handler, that evaluates the compound xml actions.
    compound = new CompoundActionHandler(this);

	logger.info("createIconActions");
        // create standard actions:
        createStandardActions();
        // icon actions:
        createIconActions();
    	logger.info("createNodeHookActions");
        //node hook actions:
        createNodeHookActions();

    	logger.info("mindmap_menus");
        // load menus:
        try {
            InputStream in;
            in = this.getFrame().getResource("mindmap_menus.xml").openStream();
            mMenuStructure = updateMenusFromXml(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            freemind.main.Resources.getInstance().logException(e);
        }

    	logger.info("MindMapPopupMenu");
        popupmenu = new MindMapPopupMenu(this);
    	logger.info("MindMapToolBar");
        toolbar = new MindMapToolBar(this);
        setAllActions(false);

        // addAsChildMode (use old model of handling CtrN) (PN)
        addAsChildMode =
            Resources.getInstance().getBoolProperty("add_as_child");
        mRegistrations = new Vector();
        Toolkit toolkit = getFrame().getViewport().getToolkit();
		selection = toolkit.getSystemSelection();
		clipboard = toolkit.getSystemClipboard();

        attributeController = new MindMapModeAttributeController(this);
        showAttributeManagerAction = getController().showAttributeManagerAction;
        propertyAction = getController().propertyAction;
        
    }

    private void createStandardActions() {
        // prepare undo:
        undo = new UndoAction(this);
        redo = new RedoAction(this);
        // register default action handler:
        // the executor must be the first here, because it is executed last then.
        getActionFactory().registerHandler(new ModeControllerActionHandler(getActionFactory()));
        getActionFactory().registerUndoHandler(new UndoActionHandler(this, undo, redo));
        //debug:          getActionFactory().registerHandler(new freemind.modes.mindmapmode.actions.xml.PrintActionHandler(this));

        cut = new CutAction(this);
        paste = new PasteAction(this);
        copy = new CopyAction(this);
        copySingle = new CopySingleAction(this);
        bold = new BoldAction (this);
        italic = new ItalicAction(this);
        underlined = new UnderlinedAction(this);
        fontSize = new FontSizeAction(this);
        fontFamily = new FontFamilyAction(this);
        edit = new EditAction(this);
        useRichFormatting = new UseRichFormattingAction(this);
        usePlainText = new UsePlainTextAction(this);
        newChild = new NewChildAction(this);
        deleteChild = new DeleteChildAction(this);
        toggleFolded = new ToggleFoldedAction(this);
        toggleChildrenFolded = new ToggleChildrenFoldedAction(this);
        nodeUp = new NodeUpAction(this);
        nodeDown = new NodeDownAction(this);
        edgeColor = new EdgeColorAction(this);
        nodeColor = new NodeColorAction(this);
        nodeColorBlend = new NodeColorBlendAction(this);
        fork = new NodeStyleAction(this, MindMapNode.STYLE_FORK);
        bubble = new NodeStyleAction(this, MindMapNode.STYLE_BUBBLE);
        // this is an unknown icon and thus corrected by mindicon:
        removeLastIconAction = new RemoveIconAction(this);
        // this action handles the xml stuff: (undo etc.)
        unknownIconAction = new IconAction(this, MindIcon.factory((String) MindIcon
                .getAllIconNames().get(0)), removeLastIconAction);
        removeLastIconAction.setIconAction(unknownIconAction);
        removeAllIconsAction = new RemoveAllIconsAction(this, unknownIconAction);
        // load pattern actions:
        loadPatternActions();
        EdgeWidth_WIDTH_PARENT = new EdgeWidthAction(this, EdgeAdapter.WIDTH_PARENT);
        EdgeWidth_WIDTH_THIN = new EdgeWidthAction(this, EdgeAdapter.WIDTH_THIN);
        EdgeWidth_1 = new EdgeWidthAction(this, 1);
        EdgeWidth_2 = new EdgeWidthAction(this, 2);
        EdgeWidth_4 = new EdgeWidthAction(this, 4);
        EdgeWidth_8 = new EdgeWidthAction(this, 8);
        edgeWidths =  new EdgeWidthAction[]{
            EdgeWidth_WIDTH_PARENT, EdgeWidth_WIDTH_THIN, EdgeWidth_1, EdgeWidth_2, EdgeWidth_4, EdgeWidth_8
        };
        EdgeStyle_linear = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_LINEAR);
        EdgeStyle_bezier = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_BEZIER);
        EdgeStyle_sharp_linear = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_SHARP_LINEAR);
        EdgeStyle_sharp_bezier = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_SHARP_BEZIER);
        edgeStyles =  new EdgeStyleAction[]{
            EdgeStyle_linear,
            EdgeStyle_bezier,
            EdgeStyle_sharp_linear,
            EdgeStyle_sharp_bezier
        };
        cloud = new CloudAction(this);
        cloudColor = new freemind.modes.mindmapmode.actions.CloudColorAction(this);
        addArrowLinkAction = new AddArrowLinkAction(this);
        removeArrowLinkAction = new RemoveArrowLinkAction(this, null);
        addArrowLinkAction.setRemoveAction(removeArrowLinkAction);
        colorArrowLinkAction = new ColorArrowLinkAction(this, null);
        changeArrowsInArrowLinkAction = new ChangeArrowsInArrowLinkAction(this, "none", null, null, true, true);
        nodeBackgroundColor = new NodeBackgroundColorAction(this);
        removeNodeBackgroundColor = new RemoveNodeBackgroundColorAction(this);
        setLinkByTextField = new SetLinkByTextFieldAction(this);
        addLocalLinkAction = new AddLocalLinkAction(this);
        gotoLinkNodeAction = new GotoLinkNodeAction(this, null);
        moveNodeAction = new MoveNodeAction(this);
        joinNodes = new JoinNodesAction(this);
        importExplorerFavorites = new ImportExplorerFavoritesAction(this);
        importFolderStructure = new ImportFolderStructureAction(this);
        changeArrowLinkEndPoints = new ChangeArrowLinkEndPoints(this);
        find = new FindAction(this);
        findNext = new FindNextAction(this,find);
        nodeHookAction = new NodeHookAction("no_title", this);
        revertAction = new RevertAction(this);
        selectBranchAction = new SelectBranchAction(this);
        selectAllAction = new SelectAllAction(this);
        
        undoableHookContentActor = new NodeHookUndoableContentActor(this);
    }

	/**
	 * Tries to load the user patterns and proposes an update to the new format,
	 * if they are old fashioned (this is determined by having an exception
	 * while reading the pattern file).
	 */
	private void loadPatternActions() {
		try {
			loadPatterns(getPatternReader());
		} catch (Exception ex) {
			System.err.println("Patterns not loaded:" + ex);
			// repair old patterns:
			String repairTitle = "Repair patterns";
			File patternsFile = getFrame().getPatternsFile();
			int result = JOptionPane
					.showConfirmDialog(
							null,
							"<html>The pattern file format has changed, <br>"
									+ "and it seems, that your pattern file<br>"
									+ "'"
									+ patternsFile.getAbsolutePath()
									+ "'<br> is formatted in the old way. <br>"
									+ "Should I try to repair the pattern file <br>"
									+ "(otherwise, you should update it by hand or delete it)?",
							repairTitle, JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				// try xslt script:
				boolean success = false;
				try {
					loadPatterns(Tools.getUpdateReader(Tools.getReaderFromFile(patternsFile),
							"patterns_updater.xslt", getFrame()));
					// save patterns directly:
					StylePatternFactory.savePatterns(new FileWriter(
							patternsFile), mPatternsList);
					success = true;
				} catch (Exception e) {
					freemind.main.Resources.getInstance().logException(e);
				}
				if (success) {
					JOptionPane.showMessageDialog(null,
							"Successfully repaired the pattern file.",
							repairTitle, JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,
							"An error occured repairing the pattern file.",
							repairTitle, JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

    /**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public Reader getPatternReader() throws FileNotFoundException, IOException {
        Reader reader = null;
        File patternsFile = getFrame().getPatternsFile();
        if (patternsFile != null && patternsFile.exists()) {
            reader = new FileReader(patternsFile);
        } else {
            System.out.println("User patterns file " + patternsFile
                    + " not found.");
            reader = new InputStreamReader(getResource("patterns.xml")
                    .openStream());
        }
        return reader;
    }

    
    public boolean isUndoAction() {
        return undo.isUndoAction() || redo.isUndoAction();
    }

    public void load(String xmlMapContents) {
        revertAction.openXmlInsteadOfMap(xmlMapContents);
    }



    /** Creates the patterns actions (saved in array patterns),
     *  and the pure patterns list (saved in mPatternsList).
     * @throws Exception
     */
    public void loadPatterns(Reader reader) throws Exception {
        createPatterns(StylePatternFactory.loadPatterns(reader));
    }

    private void createPatterns(List patternsList) throws Exception {
    		mPatternsList = patternsList;
        patterns = new ApplyPatternAction[patternsList.size()];
        for (int i = 0; i < patterns.length; i++) {
            Pattern actualPattern = (Pattern) patternsList.get(i);
			patterns[i] = new ApplyPatternAction(this,
                    actualPattern);

            // search icons for patterns:
            PatternIcon patternIcon = actualPattern
                    .getPatternIcon();
            if (patternIcon != null && patternIcon.getValue() != null) {
                patterns[i].putValue(Action.SMALL_ICON, MindIcon.factory(
                        patternIcon.getValue()).getIcon());
            }
        }
    }

    /** This method is called after and before a change of the map module.
     * Use it to perform the actions that cannot be performed at creation time.
     *
     */
    public void startupController() {
		super.startupController();
		HookFactory hookFactory = getHookFactory();
		List pluginRegistrations = hookFactory.getRegistrations();
		logger.fine("mScheduledActions are executed: "
				+ pluginRegistrations.size());
		for (Iterator i = pluginRegistrations.iterator(); i.hasNext();) {
			// call constructor:
			try {
				RegistrationContainer container = (RegistrationContainer) i
						.next();
				Class registrationClass = container.hookRegistrationClass;
				Constructor hookConstructor = registrationClass
						.getConstructor(new Class[] { ModeController.class,
								MindMap.class });
				HookRegistration registrationInstance = (HookRegistration) hookConstructor
						.newInstance(new Object[] { this, getMap() });
				// register the instance to enable basePlugins.
				hookFactory.registerRegistrationContainer(container,
						registrationInstance);
				registrationInstance.register();
				mRegistrations.add(registrationInstance);
			} catch (Exception e) {
				freemind.main.Resources.getInstance().logException(				e);
			}
		}

        // register mouse motion handler:
        getController().getMapMouseMotionListener().register(new MindMapMouseMotionManager(this));
        getController().getNodeDropListener().register(new MindMapNodeDropListener(this));
        getController().getNodeKeyListener().register(new CommonNodeKeyListener(this, new EditHandler(){

			public void edit(KeyEvent e, boolean addNew, boolean editLong) {
				MindMapController.this.edit(e, addNew, editLong);

			}}));
        getController().getNodeMotionListener().register(new MindMapNodeMotionListener(this));
        getController().getNodeMouseMotionListener().register(new CommonNodeMouseMotionListener(this));
	}

    public void shutdownController() {
        super.shutdownController();
        for (Iterator i = mRegistrations.iterator(); i.hasNext();) {
            HookRegistration registrationInstance = (HookRegistration) i.next();
            registrationInstance.deRegister();
        }
        getHookFactory().deregisterAllRegistrationContainer();
        mRegistrations.clear();
        // deregister motion handler
        getController().getMapMouseMotionListener().deregister();
        getController().getNodeDropListener().deregister();
        getController().getNodeKeyListener().deregister();
        getController().getNodeMotionListener().deregister();
        getController().getNodeMouseMotionListener().deregister();
    }

	public MapAdapter newModel(ModeController modeController) {
       return new MindMapMapModel(getFrame(), modeController); }

    private void createIconActions() {
        Vector iconNames = MindIcon.getAllIconNames();
        File iconDir = new File (Resources.getInstance().getFreemindDirectory(),"icons");
        if (iconDir.exists()){
            String[] userIconArray = iconDir.list(new FilenameFilter(){
                public boolean accept(File dir, String name) {
                    return name.matches(".*\\.png");
                }                
            });
            if(userIconArray != null)
	            for ( int i = 0 ; i < userIconArray.length; ++i ) {
	                String iconName = userIconArray[i];
	                iconName = iconName.substring(0, iconName.length()-4);
	                if(iconName.equals("")){
	                    continue;
	                }
	                iconNames.add(iconName); }
        }
        for ( int i = 0 ; i < iconNames.size(); ++i ) {
            String iconName = ((String) iconNames.get(i));
            MindIcon myIcon     = MindIcon.factory(iconName);
            IconAction myAction = new IconAction(this, myIcon,removeLastIconAction);
            iconActions.add(myAction);
        }
    }

	/**
	 *
	 */
	private void createNodeHookActions() {
        if (hookActions == null) {
            hookActions = new Vector();
            // HOOK TEST
            MindMapHookFactory factory = (MindMapHookFactory) getHookFactory();
            List nodeHookNames = factory.getPossibleNodeHooks();
            for (Iterator i = nodeHookNames.iterator(); i.hasNext();) {
                String hookName = (String) i.next();
                // create hook action.
                NodeHookAction action = new NodeHookAction(hookName, this);
                hookActions.add(action);
            }
            List modeControllerHookNames =
                factory.getPossibleModeControllerHooks();
            for (Iterator i = modeControllerHookNames.iterator(); i.hasNext();) {
                String hookName = (String) i.next();
                MindMapControllerHookAction action =
                    new MindMapControllerHookAction(hookName, this);
                hookActions.add(action);
            }
            //HOOK TEST END
	    }
	}


    public FileFilter getFileFilter() {
       return filefilter; }

    public void nodeChanged(MindMapNode n) {
    	super.nodeChanged(n);
    	// only for the selected node (fc, 2.5.2004)
		final MapModule mapModule = getController().getMapModule();
		if (mapModule != null && n == mapModule.getView().getSelected()) {
			updateToolbar(n);
		}
	 }

    public void onSelectHook(NodeView n) {
       super.onSelectHook(n);
       updateToolbar(n.getModel()); 
    }

    private void updateToolbar(MindMapNode n) {
        toolbar.selectFontSize(n.getFontSize());
        toolbar.selectFontName(n.getFontFamilyName());
    }
    
    // fc, 14.12.2004: changes, such that different models can be used:
    private NewNodeCreator myNewNodeCreator = null;
    private MindMapModeAttributeController attributeController;
    /** A general list of MindMapControllerPlugin s. Members need to be 
     * tested for the right class and casted to be applied. */
	private HashSet mPlugins = new HashSet();

    public interface NewNodeCreator {
        MindMapNode createNode(Object userObject, MindMap map);
    }

    public class DefaultMindMapNodeCreator implements NewNodeCreator {

        public MindMapNode createNode(Object userObject, MindMap map) {
            return new MindMapNodeModel(userObject, getFrame(), map);
        }

    }

    public void setNewNodeCreator(NewNodeCreator creator) {
        myNewNodeCreator = creator;
    }

    public MindMapNode newNode(Object userObject, MindMap map) {
        // singleton default:
        if (myNewNodeCreator == null) {
            myNewNodeCreator = new DefaultMindMapNodeCreator();
        }
        return myNewNodeCreator.createNode(userObject, map);
    }

    // fc, 14.12.2004: end "different models" change

	    //get/set methods



	/**
	 */
    public void updateMenus(StructuredMenuHolder holder) {

		processMenuCategory(holder, mMenuStructure.getListChoiceList(), ""); /*MenuBar.MENU_BAR_PREFIX*/
		// add hook actions to this holder.
		// hooks, fc, 1.3.2004:
		MindMapHookFactory hookFactory = (MindMapHookFactory) getHookFactory();
		for (int i = 0; i < hookActions.size(); ++i) {
			AbstractAction hookAction = (AbstractAction) hookActions.get(i);
			String hookName = ((HookAction) hookAction).getHookName();
			hookFactory.decorateAction(hookName, hookAction);
			List hookMenuPositions = hookFactory.getHookMenuPositions(hookName);
			for (Iterator j = hookMenuPositions.iterator(); j.hasNext();) {
				String pos = (String) j.next();
				holder.addMenuItem(hookFactory.getMenuItem(hookName, hookAction), pos);
			}
		}
		// update popup and toolbar:
		popupmenu.update(holder);
		toolbar.update(holder);

//		editMenu.add(getExtensionMenu());
		String formatMenuString = MenuBar.FORMAT_MENU;
        createPatternSubMenu(holder, formatMenuString);


//        editMenu.add(getIconMenu());
		addIconsToMenu(holder, MenuBar.INSERT_MENU + "icons");

    }

    public void addIconsToMenu(StructuredMenuHolder holder, String iconMenuString) {
		JMenu iconMenu = holder.addMenu(new JMenu(getText("icon_menu")), iconMenuString+"/.") ;
		holder.addAction(removeLastIconAction, iconMenuString+"/removeLastIcon");
		holder.addAction(removeAllIconsAction, iconMenuString+"/removeAllIcons");
		holder.addSeparator(iconMenuString);
		for (int i=0; i<iconActions.size(); ++i) {
			   JMenuItem item = holder.addAction((Action) iconActions.get(i), iconMenuString+"/"+i);
		}
	}

	/**
     */
    public void createPatternSubMenu(StructuredMenuHolder holder, String formatMenuString) {
        for (int i = 0; i < patterns.length; ++i) {
            JMenuItem item =
                holder.addAction(
                    patterns[i],
                    formatMenuString +"patterns/patterns/" + i);
            item.setAccelerator(
                KeyStroke.getKeyStroke(
                    getFrame().getAdjustableProperty(
                        "keystroke_apply_pattern_" + (i + 1))));
        }
    }

    public MenuStructure updateMenusFromXml(InputStream in) {
        // get from resources:
        try {
       	    IUnmarshallingContext unmarshaller = XmlBindingTools.getInstance().createUnmarshaller();
        	MenuStructure menus = (MenuStructure) unmarshaller.unmarshalDocument(in, null);
        	return menus;
        } catch (JiBXException e) {
            freemind.main.Resources.getInstance().logException(e);
            throw new IllegalArgumentException("Menu structure could not be read.");
        }
    }


    /**
     */
    public void processMenuCategory(StructuredMenuHolder holder, List list, String category) {
		String categoryCopy = category;
        ButtonGroup buttonGroup = null;
    	for (Iterator i = list.iterator(); i.hasNext();) {
            Object obj = (Object) i.next();
            if(obj instanceof MenuCategoryBase) {
				MenuCategoryBase cat = (MenuCategoryBase) obj;
            	String newCategory = categoryCopy+"/"+cat.getName();
				holder.addCategory(newCategory);
                if(cat instanceof MenuSubmenu) {
                	MenuSubmenu submenu = (MenuSubmenu) cat;
                	holder.addMenu(new JMenu(getText(submenu.getNameRef())), newCategory+"/.");
                }
            	processMenuCategory(holder, cat.getListChoiceList(), newCategory);
            } else if( obj instanceof MenuActionBase ) {
				MenuActionBase action = (MenuActionBase) obj;
				String field = action.getField();
				String name = action.getName();
				if(name == null) {
					name = field;
				}
				String keystroke = action.getKeyRef();
				try {
					Action theAction = (Action) this.getClass().getField(field).get(this);
					String theCategory = categoryCopy+"/"+name;
					if (obj instanceof MenuCheckedAction) {
						addCheckBox(holder, theCategory, theAction, keystroke);
					} else if (obj instanceof MenuRadioAction) {
                        final JRadioButtonMenuItem item = (JRadioButtonMenuItem) addRadioItem(holder, theCategory, theAction, keystroke, ((MenuRadioAction)obj).getSelected());
                        if(buttonGroup == null)
                            buttonGroup = new ButtonGroup();
                        buttonGroup.add(item);

                    } else {
						add(holder, theCategory, theAction, keystroke);
					}
				} catch (Exception e1) {
freemind.main.Resources.getInstance().logException(					e1);
				}
            } else if (obj instanceof MenuSeparator) {
            	holder.addSeparator(categoryCopy);
            } /* else exception */
         }
    }

    public JPopupMenu getPopupMenu() {
        return popupmenu;
    }

    /** Link implementation: If this is a link, we want to make a popup with at least removelink available.*/
    public JPopupMenu getPopupForModel(java.lang.Object obj) {
        if( obj instanceof MindMapArrowLinkModel) {
            // yes, this is a link.
            MindMapArrowLinkModel link = (MindMapArrowLinkModel) obj;
            JPopupMenu arrowLinkPopup = new JPopupMenu();
            // block the screen while showing popup.
            arrowLinkPopup.addPopupMenuListener( this.popupListenerSingleton );
            removeArrowLinkAction.setArrowLink(link);
            arrowLinkPopup.add(new RemoveArrowLinkAction(this, link));
            arrowLinkPopup.add(new ColorArrowLinkAction(this, link));
            arrowLinkPopup.addSeparator();
            /* The arrow state as radio buttons: */
            JRadioButtonMenuItem itemnn = new JRadioButtonMenuItem( new ChangeArrowsInArrowLinkAction(this, "none", "images/arrow-mode-none.png",link, false, false) );
            JRadioButtonMenuItem itemnt = new JRadioButtonMenuItem( new ChangeArrowsInArrowLinkAction(this, "forward", "images/arrow-mode-forward.png",link, false, true) );
            JRadioButtonMenuItem itemtn = new JRadioButtonMenuItem( new ChangeArrowsInArrowLinkAction(this, "backward", "images/arrow-mode-backward.png",link, true, false) );
            JRadioButtonMenuItem itemtt = new JRadioButtonMenuItem( new ChangeArrowsInArrowLinkAction(this, "both", "images/arrow-mode-both.png",link, true, true) );
            itemnn.setText(null);
            itemnt.setText(null);
            itemtn.setText(null);
            itemtt.setText(null);
            arrowLinkPopup.add( itemnn );
            arrowLinkPopup.add( itemnt );
            arrowLinkPopup.add( itemtn );
            arrowLinkPopup.add( itemtt );
            // select the right one:
            boolean a = !link.getStartArrow().equals("None");
            boolean b = !link.getEndArrow().equals("None");
            itemtt.setSelected(a&&b);
            itemnt.setSelected(!a&&b);
            itemtn.setSelected(a&&!b);
            itemnn.setSelected(!a&&!b);

            arrowLinkPopup.addSeparator();

            arrowLinkPopup.add(new GotoLinkNodeAction(this, link.getSource()));
            arrowLinkPopup.add(new GotoLinkNodeAction(this, link.getTarget()));

            arrowLinkPopup.addSeparator();
            // add all links from target and from source:
            HashSet NodeAlreadyVisited = new HashSet();
            NodeAlreadyVisited.add(link.getSource());
            NodeAlreadyVisited.add(link.getTarget());
            Vector links = getMindMapMapModel().getLinkRegistry().getAllLinks(link.getSource());
            links.addAll(getMindMapMapModel().getLinkRegistry().getAllLinks(link.getTarget()));
            for(int i = 0; i < links.size(); ++i) {
                MindMapArrowLinkModel foreign_link = (MindMapArrowLinkModel) links.get(i);
                if(NodeAlreadyVisited.add(foreign_link.getTarget())) {
                    arrowLinkPopup.add(new GotoLinkNodeAction(this, foreign_link.getTarget()));
                }
                if(NodeAlreadyVisited.add(foreign_link.getSource())) {
                    arrowLinkPopup.add(new GotoLinkNodeAction(this, foreign_link.getSource()));
                }
            }
            return arrowLinkPopup;
        }
        return null;
    }


    //convenience methods
    private MindMapMapModel getMindMapMapModel() {
	return (MindMapMapModel)getMap();
    }

    public JToolBar getModeToolBar() {
    		return getToolBar();
    }

    MindMapToolBar getToolBar() {
	return (MindMapToolBar)toolbar;
    }

    public Component getLeftToolBar() {
	return ((MindMapToolBar)toolbar).getLeftToolBar();
    }

    /**
     * Enabled/Disabled all actions that are dependent on
     * whether there is a map open or not.
     */
    protected void setAllActions(boolean enabled) {
    	logger.fine("setAllActions:"+enabled);
        super.setAllActions(enabled);
        // own actions
        increaseNodeFont.setEnabled(enabled);
        decreaseNodeFont.setEnabled(enabled);
        exportBranch.setEnabled(enabled);
        exportBranchToHTML.setEnabled(enabled);
        editLong.setEnabled(enabled);
        newSibling.setEnabled(enabled);
        newPreviousSibling.setEnabled(enabled);
        setLinkByFileChooser.setEnabled(enabled);
        setImageByFileChooser.setEnabled(enabled);
        followLink.setEnabled(enabled);
        for (int i=0; i<iconActions.size(); ++i) {
            ((Action) iconActions.get(i)).setEnabled(enabled);
        }
        save.setEnabled(enabled);
        saveAs.setEnabled(enabled);
        getToolBar().setAllActions(enabled);
        exportBranch.setEnabled(enabled);
        exportToHTML.setEnabled(enabled);
        importBranch.setEnabled(enabled);
        importLinkedBranch.setEnabled(enabled);
        importLinkedBranchWithoutRoot.setEnabled(enabled);
        // hooks:
		for (int i=0; i<hookActions.size(); ++i) {
			((Action) hookActions.get(i)).setEnabled(enabled);
		}
        cut.setEnabled(enabled);
        copy.setEnabled(enabled);
        copySingle.setEnabled(enabled);
        paste.setEnabled(enabled);
        undo.setEnabled(enabled);
        redo.setEnabled(enabled);
        edit.setEnabled(enabled);
        newChild.setEnabled(enabled);
        toggleFolded.setEnabled(enabled);
        toggleChildrenFolded.setEnabled(enabled);
        setLinkByTextField.setEnabled(enabled);
        italic.setEnabled(enabled);
        bold.setEnabled(enabled);
        find.setEnabled(enabled);
        findNext.setEnabled(enabled);
        addArrowLinkAction.setEnabled(enabled);
        addLocalLinkAction.setEnabled(enabled);
        nodeColorBlend.setEnabled(enabled);
        nodeUp.setEnabled(enabled);
        nodeBackgroundColor.setEnabled(enabled);
        nodeDown.setEnabled(enabled);
        importExplorerFavorites.setEnabled(enabled);
        importFolderStructure.setEnabled(enabled);
        joinNodes.setEnabled(enabled);
        deleteChild.setEnabled(enabled);
        cloud.setEnabled(enabled);
        cloudColor.setEnabled(enabled);
//        normalFont.setEnabled(enabled);
        nodeColor.setEnabled(enabled);
        edgeColor.setEnabled(enabled);
        removeLastIconAction.setEnabled(enabled);
        removeAllIconsAction.setEnabled(enabled);
        selectAllAction.setEnabled(enabled);
        selectBranchAction.setEnabled(enabled);
        removeNodeBackgroundColor.setEnabled(enabled);
        moveNodeAction.setEnabled(enabled);
        revertAction.setEnabled(enabled);
        for (int i=0; i<edgeWidths.length; ++i) {
            edgeWidths[i].setEnabled(enabled);
        }
        fork.setEnabled(enabled);
        bubble.setEnabled(enabled);
        for (int i=0; i<edgeStyles.length; ++i) {
            edgeStyles[i].setEnabled(enabled);
        }
        for (int i=0; i<patterns.length; ++i) {
            patterns[i].setEnabled(enabled);
        }
        useRichFormatting.setEnabled(enabled);
        usePlainText.setEnabled(enabled);
        editAttributes.setEnabled(enabled);
        assignAttributes.setEnabled(enabled);
    }




    //
    //      Actions
    //_______________________________________________________________________________

    // This may later be moved to ControllerAdapter. So far there is no reason for it.
    protected class ExportToHTMLAction extends AbstractAction {
        MindMapController c;
        public ExportToHTMLAction(MindMapController controller) {
            super(getText("export_to_html"));
            c = controller; }
        public void actionPerformed(ActionEvent e) {
        	// from https://sourceforge.net/tracker2/?func=detail&atid=307118&aid=1789765&group_id=7118
        	if (getMap().getFile() == null) {
				JOptionPane.showMessageDialog(getFrame().getContentPane(),
						getText("map_not_saved"), "FreeMind",
						JOptionPane.WARNING_MESSAGE);
				return;
			}             	
            try {
                File file = new File(c.getMindMapMapModel().getFile() + ".html");
                saveHTML((MindMapNodeModel) c.getMindMapMapModel().getRoot(), file);
                loadURL(file.toString());
            }
            catch (IOException ex) {
				freemind.main.Resources.getInstance().logException(ex);
            }
        }
    }

    protected class ExportBranchToHTMLAction extends AbstractAction {
        MindMapController c;
        public ExportBranchToHTMLAction(MindMapController controller) {
            super(getText("export_branch_to_html"));
            c = controller; 
        }
        public void actionPerformed(ActionEvent e) {
        	if (getMap().getFile() == null) {
        		JOptionPane.showMessageDialog(getFrame().getContentPane(),
        				getText("map_not_saved"), "FreeMind",
        				JOptionPane.WARNING_MESSAGE);
        		return;
        	}             	
            try {
                File file = File.createTempFile("tmm", ".html");
                saveHTML((MindMapNodeModel)getSelected(),file);
                loadURL(file.toString()); 
            }
            catch (IOException ex) {
            }
        }
    }

    private class ImportBranchAction extends AbstractAction {
	ImportBranchAction() {
           super(getText("import_branch")); }
	public void actionPerformed(ActionEvent e) {
	    MindMapNodeModel parent = (MindMapNodeModel)getSelected();
	    if (parent == null) {
	           return; }
	        JFileChooser chooser = new JFileChooser();
	        //chooser.setLocale(currentLocale);
	        if (getFileFilter() != null) {
	           chooser.addChoosableFileFilter(getFileFilter()); }
	        int returnVal = chooser.showOpenDialog(getFrame().getContentPane());
	        if (returnVal==JFileChooser.APPROVE_OPTION) {
	           try {
	              MindMapNodeModel node = getMindMapMapModel().loadTree(chooser.getSelectedFile());
	              paste(node, parent);
				  invokeHooksRecursively(node, getMindMapMapModel());
	           }
	           catch (Exception ex) {
	              handleLoadingException(ex); }}}}

    private class ImportLinkedBranchAction extends AbstractAction {
	ImportLinkedBranchAction() {
           super(getText("import_linked_branch")); }
	public void actionPerformed(ActionEvent e) {
	    MindMapNodeModel selected = (MindMapNodeModel)getSelected();
	    if (selected == null || selected.getLink() == null) {
	        JOptionPane.showMessageDialog(getView(),getText("import_linked_branch_no_link"));
            return;
        }
            URL absolute = null;
            try {
               String relative = selected.getLink();
               absolute = Tools.isAbsolutePath(relative) ? Tools.fileToUrl(new File(relative)) :
                  new URL(Tools.fileToUrl(getMap().getFile()), relative); }
            catch (MalformedURLException ex) {
               JOptionPane.showMessageDialog(getView(),"Couldn't create valid URL for:"+getMap().getFile());
               freemind.main.Resources.getInstance().logException(ex);
               return; }
            try {
            	MindMapNodeModel node = getMindMapMapModel().loadTree(Tools.urlToFile(absolute));
               paste(node, selected);
			   invokeHooksRecursively(node, getMindMapMapModel());
            }
            catch (Exception ex) {
               handleLoadingException(ex); }}}

    /**
     * This is exactly the opposite of exportBranch.
     */
    private class ImportLinkedBranchWithoutRootAction extends AbstractAction {
	ImportLinkedBranchWithoutRootAction() {
           super(getText("import_linked_branch_without_root")); }
	public void actionPerformed(ActionEvent e) {
	    MindMapNodeModel selected = (MindMapNodeModel)getSelected();
	    if (selected == null || selected.getLink() == null) {
	        JOptionPane.showMessageDialog(getView(),getText("import_linked_branch_no_link"));
            return;
        }
            URL absolute = null;
            try {
               String relative = selected.getLink();
               absolute = Tools.isAbsolutePath(relative) ? Tools.fileToUrl(new File(relative)) :
                  new URL(Tools.fileToUrl(getMap().getFile()), relative); }
            catch (MalformedURLException ex) {
               JOptionPane.showMessageDialog(getView(),"Couldn't create valid URL.");
               return; }
            try {
               MindMapNodeModel node = getMindMapMapModel().loadTree(Tools.urlToFile(absolute));
               for (ListIterator i = node.childrenUnfolded();i.hasNext();) {
                  	MindMapNodeModel importNode = (MindMapNodeModel)i.next();
					paste(importNode, selected);
					invokeHooksRecursively(importNode, getMindMapMapModel());
                  }
               }
               //getModel().setLink(parent, null); }
            catch (Exception ex) {
               handleLoadingException(ex); }}}


/*
 MindMapController.java
    private class NodeViewStyleAction extends AbstractAction {
       NodeViewStyleAction(final String style) { super(getText(style)); m_style = style; }
       public void actionPerformed(ActionEvent e) {
          for(ListIterator it = getSelecteds().listIterator();it.hasNext();) {
             MindMapNodeModel selected = (MindMapNodeModel)it.next();
             getModel().setNodeStyle(selected, m_style); }}
       private String m_style;}

    private class EdgeStyleAction extends AbstractAction {
	String style;
	EdgeStyleAction(String style) {
	    super(getText(style));
            this.style = style; }
       public void actionPerformed(ActionEvent e) {
          for(ListIterator it = getSelecteds().listIterator();it.hasNext();) {
             MindMapNodeModel selected = (MindMapNodeModel)it.next();
             getModel().setEdgeStyle(selected, style); }}}

    private class ApplyPatternAction extends AbstractAction {
        StylePattern pattern;
	ApplyPatternAction(StylePattern pattern) {
	    super(pattern.getName());
	    this.pattern=pattern; }
	public void actionPerformed(ActionEvent e) {
	    for(ListIterator it = getSelecteds().listIterator();it.hasNext();) {
		MindMapNodeModel selected = (MindMapNodeModel)it.next();
                ((MindMapMapModel)getModel()).applyPattern(selected, pattern); }}}




    // Nonaction classes
    // ________________________________________________________________________

*/
    private class MindMapFilter extends FileFilter {
      public boolean accept(File f) {
	      if (f.isDirectory()) return true;
	      String extension = Tools.getExtension(f.getName());
	      if (extension != null) {
		  if (extension.equals(freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT)) {
		     return true;
		  } else {
		     return false;
		  }
	      }
	      return false;
       }

	   public String getDescription() {
	      return getText("mindmaps_desc");
	   }
    }

    public void setBold(MindMapNode node, boolean bolded) {
        bold.setBold(node, bolded);
    }

    public void setItalic(MindMapNode node, boolean isItalic) {
        italic.setItalic(node, isItalic);
    }

    public void setCloud(MindMapNode node, boolean enable) {
        cloud.setCloud(node, enable);
    }
    public void setCloudColor(MindMapNode node, Color color) {
        cloudColor.setCloudColor(node, color);
    }
    //Node editing
    public void setFontSize(MindMapNode node, String fontSizeValue) {
        fontSize.setFontSize(node, fontSizeValue);
    }

    /**
     *
     */

    public void increaseFontSize(MindMapNode node, int increment) {
        int newSize = Integer.valueOf(node.getFontSize()).intValue()+increment;

        if (newSize > 0) {
            setFontSize(node, Integer.toString(newSize));
        }
    }

    public void setFontFamily(MindMapNode node, String fontFamilyValue) {
        fontFamily.setFontFamily(node, fontFamilyValue);
    }

    public void setNodeColor(MindMapNode node, Color color) {
        nodeColor.setNodeColor(node, color);
    }

    public void setNodeBackgroundColor(MindMapNode node, Color color) {
        nodeBackgroundColor.setNodeBackgroundColor(node, color);
    }
    public void blendNodeColor(MindMapNode node) {
        Color mapColor = getView().getBackground();
        Color nodeColor = node.getColor();
        if (nodeColor == null) {
            nodeColor = MapView.standardNodeTextColor;
        }
        setNodeColor(node, new Color(
                (3 * mapColor.getRed() + nodeColor.getRed()) / 4, (3 * mapColor
                        .getGreen() + nodeColor.getGreen()) / 4, (3 * mapColor
                        .getBlue() + nodeColor.getBlue()) / 4));
    }


    public void setEdgeColor(MindMapNode node, Color color) {
        edgeColor.setEdgeColor(node, color);
    }

    public void applyPattern(MindMapNode node, String patternName){
        for (int i = 0; i < patterns.length; i++) {
            ApplyPatternAction patternAction = patterns[i];
            if(patternAction.getPattern().getName().equals(patternName)){
                patternAction.applyPattern(node, patternAction.getPattern());
                break;
            }
        }
    }



    public void applyPattern(MindMapNode node, Pattern pattern) {
        if(patterns.length > 0) {
            patterns[0].applyPattern(node, pattern);
        } else {
            throw new IllegalArgumentException("No pattern defined.");
        }
    }

    public void addIcon(MindMapNode node, MindIcon icon) {
        unknownIconAction.addIcon(node, icon);
    }


    public void removeAllIcons(MindMapNode node) {
        removeAllIconsAction.removeAllIcons(node);
    }

    public int removeLastIcon(MindMapNode node) {
        return removeLastIconAction.removeLastIcon(node);
    }
    /**
     *
     */

    public void addLink(MindMapNode source, MindMapNode target) {
        addArrowLinkAction.addLink(source, target);
    }

    public void removeReference(MindMapLink arrowLink){
        removeArrowLinkAction.removeReference(arrowLink);
    }

    public void setArrowLinkColor(MindMapLink arrowLink, Color color) {
        colorArrowLinkAction.setArrowLinkColor(arrowLink, color);
    }

    /**
     *
     */

    public void changeArrowsOfArrowLink(MindMapArrowLinkModel arrowLink,
            boolean hasStartArrow, boolean hasEndArrow) {
        changeArrowsInArrowLinkAction.changeArrowsOfArrowLink(arrowLink, hasStartArrow, hasEndArrow);
    }

    public void setArrowLinkEndPoints(MindMapArrowLink link, Point startPoint,
            Point endPoint) {
        changeArrowLinkEndPoints.setArrowLinkEndPoints(link, startPoint, endPoint);
    }
    public void setLink(MindMapNode node, String link) {
        setLinkByTextField.setLink(node, link);
    }
    /**
     *
     */

    public void setToolTip(MindMapNode node, String key, String value) {
        node.setToolTip(key, value);
        nodeRefresh(node);
    }
    // edit begins with home/end or typing (PN 6.2)
    public void edit(KeyEvent e, boolean addNew, boolean editLong) {
        edit.edit(e, addNew, editLong);
    }


    public void setNodeText(MindMapNode selected, String newText) {
        edit.setNodeText(selected, newText);
    }

    /**
     *
     */

    public void setEdgeWidth(MindMapNode node, int width) {
        EdgeWidth_1.setEdgeWidth(node, width);
    }
    /**
     *
     */

    public void setEdgeStyle(MindMapNode node, String style) {
        EdgeStyle_bezier.setEdgeStyle(node, style);
    }
    /**
     *
     */

    public void setNodeStyle(MindMapNode node, String style) {
        fork.setStyle(node, style);
    }
    
    public Transferable copy(MindMapNode node, boolean saveInvisible) {
        StringWriter stringWriter = new StringWriter();
        try {
           ((MindMapNodeModel)node).save(stringWriter, getMap().getLinkRegistry(), saveInvisible, true); }
        catch (IOException e) {}
        return new MindMapNodesSelection(stringWriter.toString(), null, null, null, null, null); 
    }

     public Transferable cut() {
        return cut(getView().getSelectedNodesSortedByY());
    }



    public Transferable cut(List nodeList) {
        return cut.cut(nodeList);
    }
    public void paste(Transferable t, MindMapNode parent) {
        paste(t, /*target=*/parent, /*asSibling=*/ false, parent.isNewChildLeft()); }

    /** @param isLeft determines, whether or not the node is placed on the left or right. 
     * @return true, if successfully. **/
    public boolean paste(Transferable t, MindMapNode target, boolean asSibling, boolean isLeft) {
    	if (! asSibling && target.isFolded() && Resources.getInstance().getBoolProperty(RESOURCE_UNFOLD_ON_PASTE)) {
			setFolded(target, false);
		   }
        return paste.paste(t, target, asSibling, isLeft);
    }

    public void paste(MindMapNode node, MindMapNode parent) {
        paste.paste(node, parent);
    }

    public MindMapNode addNew(final MindMapNode target, final int newNodeMode, final KeyEvent e) {
    	edit.stopEditing();
        return newChild.addNew(target, newNodeMode, e);
    }

    public  MindMapNode addNewNode(MindMapNode parent, int index, boolean newNodeIsLeft) {
        return newChild.addNewNode(parent, index, newNodeIsLeft);
    }


    public void deleteNode(MindMapNode selectedNode) {
        deleteChild.deleteNode(selectedNode);
    }

    public void toggleFolded() {
        toggleFolded.toggleFolded();
    }

    public void setFolded(MindMapNode node, boolean folded) {
        toggleFolded.setFolded(node, folded);
    }

    public void moveNodes(MindMapNode selected, List selecteds, int direction){
        nodeUp.moveNodes(selected, selecteds, direction);
    }

    public void joinNodes(MindMapNode selectedNode, List selectedNodes) {
        joinNodes.joinNodes(selectedNode, selectedNodes);
    }

    protected void setLinkByFileChooser() {
        String relative = getLinkByFileChooser(getFileFilter());
        if (relative != null)
            setLink((NodeAdapter) getSelected(),relative);
    }

    protected void setImageByFileChooser() {
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("jpg");
        filter.addExtension("jpeg");
        filter.addExtension("png");
        filter.addExtension("gif");
        filter.setDescription("JPG, PNG and GIF Images");

                // Are there any selected nodes with pictures?
                boolean picturesAmongSelecteds = false;
                for (ListIterator e = getSelecteds().listIterator();e.hasNext();) {
                   String link = ((MindMapNode)e.next()).getLink();
                   if (link != null) {
                      if (filter.accept(new File(link))) {
                         picturesAmongSelecteds = true;
                         break;
                      }
                   }
                }

                try {
                    // FIXME: Is this used?????
                   if (picturesAmongSelecteds) {
                      for (ListIterator e = getSelecteds().listIterator();e.hasNext();) {
                         MindMapNode node = (MindMapNode)e.next();
                         if (node.getLink() != null) {
                            String possiblyRelative = node.getLink();
                            String relative = Tools.isAbsolutePath(possiblyRelative) ?
                            		Tools.fileToUrl(new File(possiblyRelative)).toString() : possiblyRelative;
                            if (relative != null) {
                               String strText = "<html><img src=\"" + relative + "\">";
                               setLink(node, null);
                               setNodeText(node, strText);
                            }
                         }
                      }
                   }
                   else {
                      String relative = getLinkByFileChooser(filter);
                      if (relative != null) {
                         String strText = "<html><img src=\"" + relative + "\">";
                         setNodeText((MindMapNode)getSelected(),strText);
                      }
                   }
                }
                catch (MalformedURLException e){ freemind.main.Resources.getInstance().logException(e); }
    }

    protected String getLinkByFileChooser(FileFilter fileFilter) {
        URL link;
        String relative = null;
        File input;
        JFileChooser chooser = null;
        if (getMap().getFile() == null) {
            JOptionPane.showMessageDialog(getFrame().getContentPane(), getText("not_saved_for_link_error"),
                                          "FreeMind", JOptionPane.WARNING_MESSAGE);
            return null;
            // In the previous version Freemind automatically displayed save
            // dialog. It happened very often, that user took this save
            // dialog to be an open link dialog; as a result, the new map
            // overwrote the linked map.

        }
        if (getLastCurrentDir() != null) {
            chooser = new JFileChooser(getLastCurrentDir());
        } else {
            chooser = new JFileChooser();
        }

        if (fileFilter != null) {
           // Set filters, make sure AcceptAll filter comes first
           chooser.setFileFilter(fileFilter);
        } else {
            chooser.setFileFilter(chooser.getAcceptAllFileFilter());
        }

        int returnVal = chooser.showOpenDialog(getFrame().getContentPane());
        if (returnVal==JFileChooser.APPROVE_OPTION) {
            input = chooser.getSelectedFile();
            setLastCurrentDir(input.getParentFile());
            try {
                link = Tools.fileToUrl(input);
                relative = link.toString();
            } catch (MalformedURLException ex) {
                getController().errorMessage(getText("url_error"));
                return null;
            }
            if (getFrame().getProperty("links").equals("relative")) {
                //Create relative URL
                try {
                    relative = Tools.toRelativeURL(Tools.fileToUrl(getMap().getFile()), link);
                } catch (MalformedURLException ex) {
                    getController().errorMessage(getText("url_error"));
                    return null;
                }
            }
        }
        return relative;
    }

    public void loadURL(String relative) {
        if (getMap().getFile() == null) {
            getFrame().out("You must save the current map first!");
            boolean result = save();
            // canceled??
            if(!result) {
                return;
            }
         }
        super.loadURL(relative);
    }

    public void addHook(MindMapNode focussed, List selecteds, String hookName) {
        nodeHookAction.addHook(focussed, selecteds, hookName);
    }

    protected class SetLinkByFileChooserAction extends AbstractAction {
        public SetLinkByFileChooserAction() {
            super(getText("set_link_by_filechooser"));
        }
        public void actionPerformed(ActionEvent e) {
            setLinkByFileChooser();
        }
    }

    protected class SetImageByFileChooserAction extends AbstractAction {
        public SetImageByFileChooserAction() {
            super(getText("set_image_by_filechooser"));
        }
        public void actionPerformed(ActionEvent e) {
                       setImageByFileChooser();
                        getController().obtainFocusForSelected();
        }
    }

    protected class FollowLinkAction extends AbstractAction implements MenuItemEnabledListener {
        public FollowLinkAction() {
            super(getText("follow_link"));
        }
        public void actionPerformed(ActionEvent e) {
        	for (Iterator iterator = getSelecteds().iterator(); iterator.hasNext();) {
				MindMapNode selNode = (MindMapNode) iterator.next();
				if(selNode.getLink() != null) {
					loadURL(selNode.getLink());
				}
			}
        }
		public boolean isEnabled(JMenuItem pItem, Action pAction) {
			for (Iterator iterator = getSelecteds().iterator(); iterator.hasNext();) {
				MindMapNode selNode = (MindMapNode) iterator.next();
				if( selNode.getLink() != null ) 
					return true;
			}			
			return false;
		}
    }


    public void moveNodePosition(MindMapNode node, int parentVGap, int hGap,
            int shiftY) {
        moveNodeAction.moveNodeTo(node, parentVGap, hGap, shiftY);
    }

    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////


    public void plainClick(MouseEvent e) {
        /* perform action only if one selected node.*/
        if(getSelecteds().size() != 1)
            return;
        final MainView component = (MainView)e.getComponent();
        if (component.isInFollowLinkRegion(e.getX())) {
            loadURL(); }
        else {
    		MindMapNode node = (component).getNodeView().getModel();
            if (!node.hasChildren()) {
                // then emulate the plain click.
                doubleClick(e);
                return;
            }
            toggleFolded();
        }
    }

	public HookFactory getHookFactory() {
		// lazy creation.
		if(nodeHookFactory == null) {
			nodeHookFactory = new MindMapHookFactory(getFrame());
		}
		return nodeHookFactory;
	}

    public NodeHook createNodeHook(String hookName, MindMapNode node,
            MindMap map) {
        HookFactory hookFactory = getHookFactory();
        NodeHook hook = (NodeHook) hookFactory.createNodeHook(hookName);
        hook.setController(this);
        hook.setMap(map);
        if (hook instanceof PermanentNodeHook) {
            PermanentNodeHook permHook = (PermanentNodeHook) hook;
            if(hookFactory.getInstanciationMethod(hookName).isSingleton()) {
                // search for already instanciated hooks of this type:
                PermanentNodeHook otherHook = hookFactory.getHookInNode(node, hookName);
                if(otherHook != null) {
                    return otherHook;
                }
            }
            node.addHook(permHook);
        }
        return hook;
    }

    public void invokeHook(ModeControllerHook hook) {
		try {
			hook.setController(this);
			// initialize:
			// the main invocation:
			hook.startupMapHook();
			// and good bye.
			hook.shutdownMapHook();
		} catch (Exception e) {
			freemind.main.Resources.getInstance().logException(e);
		}        
    }

    public ActionFactory getActionFactory() {
        return actionFactory;
    }

    protected class EditLongAction extends AbstractAction {
        public EditLongAction() {
            super(getText("edit_long_node"));
        }
        public void actionPerformed(ActionEvent e) {
            edit(null, false, true);
        }
    }

    static public void saveHTML(MindMapNodeModel rootNodeOfBranch, File file) throws IOException {
        BufferedWriter fileout = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file)));
        MindMapHTMLWriter htmlWriter = new MindMapHTMLWriter(
                fileout);
        htmlWriter.saveHTML(rootNodeOfBranch);      
    }
    
    static public void saveHTML(List mindMapNodes, Writer fileout) throws IOException {
        MindMapHTMLWriter htmlWriter = new MindMapHTMLWriter(
                fileout);
        htmlWriter.saveHTML(mindMapNodes);      
    }   
    /**
     */
    public void splitNode(MindMapNode node, int caretPosition, String newText) {
        if (node.isRoot()) {
            return;
        }
        // If there are children, they go to the node below
        String futureText = newText != null ? newText : node.toString();

        String[] strings = getContent(futureText, caretPosition);
        if (strings == null) { // do nothing
            return;
        }
        String newUpperContent = strings[0];
        String newLowerContent = strings[1];
        setNodeText(node, newUpperContent);

        MindMapNode parent = node.getParentNode();
        MindMapNode lowerNode = addNewNode(parent, parent
                .getChildPosition(node) + 1, node.isLeft());
        lowerNode.setColor(node.getColor());
        lowerNode.setFont(node.getFont());
        setNodeText(lowerNode, newLowerContent);

    }

    private String[] getContent(String text, int pos) {
        if (pos <= 0) {
            return null;
        }
        String[] strings = new String[2];
        if (text.startsWith("<html>")) {
            HTMLEditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = new HTMLDocument();
            StringReader buf = new StringReader(text);
            try {
                kit.read(buf, doc, 0);
                final char[] firstText = doc.getText(0, pos).toCharArray();
                int firstStart = 0;
                int firstLen = pos;
                while ((firstStart < firstLen) && (firstText[firstStart] <= ' ')) {
                    firstStart++;
                }
                while ((firstStart < firstLen) && (firstText[firstLen - 1] <= ' ')) {
                    firstLen--;
                }
                int secondStart = 0;
                int secondLen = doc.getLength() - pos;
                final char[] secondText = doc.getText(pos, secondLen).toCharArray();
                while ((secondStart < secondLen) && (secondText[secondStart] <= ' ')) {
                    secondStart++;
                }
                while ((secondStart < secondLen) && (secondText[secondLen - 1] <= ' ')) {
                    secondLen--;
                }
                if (firstStart == firstLen || secondStart == secondLen) {
                    return null;
                }
                StringWriter out = new StringWriter();
                new FixedHTMLWriter(out, doc, firstStart, firstLen - firstStart).write();                
                strings[0] = out.toString();
                out = new StringWriter();
                new FixedHTMLWriter(out, doc, pos + secondStart, secondLen - secondStart).write();
                strings[1] = out.toString();
                return strings;
            }
            catch (IOException e) {
				// TODO Auto-generated catch block
				freemind.main.Resources.getInstance().logException(e);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				freemind.main.Resources.getInstance().logException(e);
			}
        }
        else {
            if (pos >= text.length()) {
                return null;
            }
            strings[0] = text.substring(0, pos);
            strings[1] = text.substring(pos);
        }
        return strings;
    }

    protected void updateNode(MindMapNode node) {
        super.updateNode(node);
        recursiveCallUpdateHooks((MindMapNode) node, (MindMapNode) node /* self update */);
    }

    /**
     */
    private void recursiveCallUpdateHooks(MindMapNode node,
            MindMapNode changedNode) {
        // Tell any node hooks that the node is changed:
        if (node instanceof MindMapNode) {
            for (Iterator i = ((MindMapNode) node).getActivatedHooks()
                    .iterator(); i.hasNext();) {
                PermanentNodeHook hook = (PermanentNodeHook) i.next();
                if ((!isUndoAction()) || hook instanceof UndoEventReceiver) {
                    if (node == changedNode)
                        hook.onUpdateNodeHook();
                    else
                        hook.onUpdateChildrenHook(changedNode);
                }
            }
        }
        if (!node.isRoot() && node.getParentNode() != null)
            recursiveCallUpdateHooks(node.getParentNode(), changedNode);
    }

    public void doubleClick(MouseEvent e) {
        /* perform action only if one selected node.*/
        if (getSelecteds().size() != 1)
            return;
        MindMapNode node = ((MainView)e.getComponent()).getNodeView().getModel();
        // edit the node only if the node is a leaf (fc 0.7.1), or the root node (fc 0.9.0)
        if (!e.isAltDown() && !e.isControlDown() && !e.isShiftDown()
                && !e.isPopupTrigger() && e.getButton() == MouseEvent.BUTTON1
                && (node.getLink() == null)) {
            edit(null, false, false);
        }
    }

    public void select(NodeView node) {
        if (node == null) {
            logger.warning("Select with null NodeView called!");
            return;
        }
        getView().scrollNodeToVisible(node);
        getView().selectAsTheOnlyOneSelected(node);
        getView().setSiblingMaxLevel(node.getModel().getNodeLevel()); // this level is default
    }

    public void selectMultipleNodes(NodeView focussed, Collection selecteds) {
        selectMultipleNodesImpl(focussed, selecteds);
    }
    public void selectMultipleNodes(MindMapNode focussed, Collection selecteds) {
        selectMultipleNodesImpl(focussed, selecteds);
    }
    private void selectMultipleNodesImpl(Object focussed, Collection selecteds) {
        // are they visible?
        for (Iterator i = selecteds.iterator(); i.hasNext();) {
            MindMapNode node = (MindMapNode)(i.next());
            displayNode(node);
        }
        // this one must be visible.
        select(getNodeView(focussed));
        for (Iterator i = selecteds.iterator(); i.hasNext();) {
            NodeView node = getNodeView(i.next());
            getView().makeTheSelected(node);
        }
        getController().obtainFocusForSelected(); // focus fix
    }

    private NodeView getNodeView(Object object) {
        if(object instanceof NodeView){
            return (NodeView)object;
        }
        if(object instanceof MindMapNode){
            return getView().getNodeView((MindMapNode)object);
        }
        throw new ClassCastException();
    }

    public void selectBranch(NodeView selected, boolean extend) {
        displayNode(selected.getModel());
        getView().selectBranch(selected, extend);
    }

    public boolean extendSelection(MouseEvent e) {
        NodeView newlySelectedNodeView = ((MainView) e.getComponent()).getNodeView();
        //MindMapNode newlySelectedNode = newlySelectedNodeView.getModel();
        boolean extend = e.isControlDown();
        // Fixes Cannot select multiple single nodes * https://sourceforge.net/tracker/?func=detail&atid=107118&aid=1675829&group_id=7118
        if(Tools.isMacOsX()) {
        	extend |= e.isMetaDown();
        }
        boolean range = e.isShiftDown();
        boolean branch = e.isAltGraphDown() || e.isAltDown(); /* windows alt, linux altgraph .... */
        boolean retValue = false;
        
        if (extend || range || branch
                || !getView().isSelected(newlySelectedNodeView)) {
            if (!range) {
                if (extend)
                    getView().toggleSelected(newlySelectedNodeView);
                else
                    select(newlySelectedNodeView);
                retValue = true;
            }
            else {
                retValue = getView().selectContinuous(newlySelectedNodeView);
                //                 /* fc, 25.1.2004: replace getView by controller methods.*/
                //                 if (newlySelectedNodeView != getView().getSelected() &&
                //                     newlySelectedNodeView.isSiblingOf(getView().getSelected())) {
                //                     getView().selectContinuous(newlySelectedNodeView);
                //                     retValue = true;
                //                 } else {
                //                     /* if shift was down, but no range can be selected, then the new node is simply selected: */
                //                     if(!getView().isSelected(newlySelectedNodeView)) {
                //                         getView().toggleSelected(newlySelectedNodeView);
                //                         retValue = true;
                //                     }
            }
            if (branch) {
                getView().selectBranch(newlySelectedNodeView, extend);
                retValue = true;
            }
        }

        if (retValue) {
            e.consume();

            // Display link in status line
            String link = newlySelectedNodeView.getModel().getLink();
            link = (link != null ? link : " ");
            getController().getFrame().out(link);
        }
        logger.fine("MouseEvent: extend:"+extend+", range:"+range+", branch:"+branch+", event:"+e+", retValue:"+retValue);
        return retValue;
    }

    public void registerMouseWheelEventHandler(MouseWheelEventHandler handler) {
        logger.fine("Registered   MouseWheelEventHandler " + handler);
        mRegisteredMouseWheelEventHandler.add(handler);
    }

    public void deRegisterMouseWheelEventHandler(MouseWheelEventHandler handler) {
        logger.fine("Deregistered MouseWheelEventHandler " + handler);
        mRegisteredMouseWheelEventHandler.remove(handler);
    }

    public Set getRegisteredMouseWheelEventHandler() {
        return Collections.unmodifiableSet(mRegisteredMouseWheelEventHandler);

    }

    public String marshall(XmlAction action) {
        return XmlBindingTools.getInstance().marshall(action);
    }

    public XmlAction unMarshall(String inputString) {
        return XmlBindingTools.getInstance().unMarshall(inputString);
    }

    public void storeDialogPositions(JDialog dialog,
            WindowConfigurationStorage pStorage,
            String window_preference_storage_property) {
        XmlBindingTools.getInstance().storeDialogPositions(getController(),
                dialog, pStorage, window_preference_storage_property);
    }

    public WindowConfigurationStorage decorateDialog(JDialog dialog,
            String window_preference_storage_property) {
        return XmlBindingTools.getInstance().decorateDialog(getController(),
                dialog, window_preference_storage_property);
    }

    public AttributeController getAttributeController() {
        return attributeController;
    }

    public AttributePopupMenu getAttributeTablePopupMenu() {
        return new AttributePopupMenu();
    }

    public XMLElement createXMLElement() {
        return new MindMapXMLElement(this);
    }

    public void setAttribute(MindMapNode pNode, int pPosition,
            Attribute pAttribute) {
    	pNode.createAttributeTableModel();
        pNode.getAttributes().setValueAt(pAttribute.getName(), pPosition, 0);
        pNode.getAttributes().setValueAt(pAttribute.getValue(), pPosition, 1);
    }

    public void removeAttribute(MindMapNode pNode, int pPosition){
    	pNode.createAttributeTableModel();
    	pNode.getAttributes().getAttributeController().performRemoveRow(pNode.getAttributes(), pPosition);
    }

    
    public int addAttribute(MindMapNode node, Attribute pAttribute){
    	node.createAttributeTableModel();
        NodeAttributeTableModel attributes = node.getAttributes();                         
        int rowCount = attributes.getRowCount();
		attributes.getAttributeController().performInsertRow(attributes, rowCount, pAttribute.getName(), pAttribute.getValue());
		return rowCount;
    }

    public int editAttribute(MindMapNode pNode, String pName, String pNewValue){
    	pNode.createAttributeTableModel();
    	Attribute newAttribute = new Attribute(pName, pNewValue);
    	NodeAttributeTableModel attributes = pNode.getAttributes();
    	for (int i = 0; i < attributes.getRowCount(); i++) {
			if(pName.equals(attributes.getAttribute(i).getName())) {
				if (pNewValue != null) {
					setAttribute(pNode, i, newAttribute);
				} else {
					// remove the attribute:
					removeAttribute(pNode, i);
				}
				return i;
			}
		}
    	if(pNewValue == null) {
    		// nothing to remove found.
    		return -1;
    	}
    	return addAttribute(pNode, newAttribute);
    }
    
    public void insertNodeInto(MindMapNode newNode, MindMapNode parent, int index) {
        getModel().setSaved(false);
        super.insertNodeInto(newNode, parent, index);
    }
    
    public void removeNodeFromParent(MindMapNode selectedNode) {
        getModel().setSaved(false);
        getModel().removeNodeFromParent(selectedNode);        
    }

	public void nodeStyleChanged(MindMapNode node) {
		nodeChanged(node);
		final ListIterator childrenFolded = node.childrenFolded();
		while(childrenFolded.hasNext()){
			MindMapNode child = (MindMapNode) childrenFolded.next();
			if( ! (child.hasStyle() && child.getEdge().hasStyle())){
				nodeStyleChanged(child);
			}
		}
	}

	public void repaintMap() {
		 getView().repaint();		
	}

	public void clearNodeContents(MindMapNode pNode) {
		Pattern erasePattern = new Pattern();
		erasePattern.setPatternEdgeColor(new PatternEdgeColor());
		erasePattern.setPatternEdgeStyle(new PatternEdgeStyle());
		erasePattern.setPatternEdgeWidth(new PatternEdgeWidth());
		erasePattern.setPatternIcon(new PatternIcon());
		erasePattern.setPatternNodeBackgroundColor(new PatternNodeBackgroundColor());
		erasePattern.setPatternNodeColor(new PatternNodeColor());
		erasePattern.setPatternNodeFontBold(new PatternNodeFontBold());
		erasePattern.setPatternNodeFontItalic(new PatternNodeFontItalic());
		erasePattern.setPatternNodeFontName(new PatternNodeFontName());
		erasePattern.setPatternNodeFontSize(new PatternNodeFontSize());
		erasePattern.setPatternNodeStyle(new PatternNodeStyle());
		erasePattern.setPatternNodeText(new PatternNodeText());
		applyPattern(pNode, erasePattern);
		List attributeKeyList = pNode.getAttributeKeyList();
		for (Iterator iterator = attributeKeyList.iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			this.getAttributeController().performRemoveAttribute(key);
			
		}
		setNoteText(pNode, null);
	}
    public EditNoteToNodeAction createEditNoteToNodeAction(
            MindMapNode node, String text) {
        EditNoteToNodeAction nodeAction = new EditNoteToNodeAction();
        nodeAction.setNode(node.getObjectId(this));
        if (text != null &&  (HtmlTools.htmlToPlain(text).length() != 0 || text.indexOf("<img") >= 0 )){
        	nodeAction.setText(text);
        }
        else{
        	nodeAction.setText(null);
        }
        return nodeAction;
    }

	public void setNoteText(MindMapNode node, String text) {
        String oldNoteText = node.getNoteText();
        if(Tools.safeEquals(text, oldNoteText)) {
            // they are equal.
            return;
        }
        logger.fine("Old Note Text:'" + oldNoteText + ", new:'" + text + "'.");
        logger.fine(Tools.compareText(oldNoteText, text));
        EditNoteToNodeAction doAction = createEditNoteToNodeAction(node,
                text);
        EditNoteToNodeAction undoAction = createEditNoteToNodeAction(node,
                oldNoteText);
        getActionFactory().startTransaction(
                ACCESSORIES_PLUGINS_NODE_NOTE);
        getActionFactory().executeAction(
                new ActionPair(doAction, undoAction));
        getActionFactory().endTransaction(
        		ACCESSORIES_PLUGINS_NODE_NOTE);

	}

	public void registerPlugin(MindMapControllerPlugin pPlugin){
		mPlugins.add(pPlugin);
	}
	public void deregisterPlugin(MindMapControllerPlugin pPlugin){
		mPlugins.remove(pPlugin);
	}

	public Set getPlugins() {
		return Collections.unmodifiableSet(mPlugins);
	}

	/**
	 */
	public Transferable getClipboardContents() {
		return clipboard.getContents(this);
	}

	/**
	 */
	public void setClipboardContents(Transferable t) {
		clipboard.setContents(t, null);
		if(selection != null){
			selection.setContents(t, null);
		}
	}
	
}
