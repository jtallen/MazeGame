package falstad;

import java.awt.*;

/**
 * Add functionality for double buffering to an AWT Panel class.
 * Used for drawing a maze.
 *
 * Post-graphics refactor, this contains all of the functionality for AWT
 * functions, encapsulating it all away from other classes.
 *
 */
public class MazePanel extends Panel  {
	/* Panel operates a double buffer see
	 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
	 * for details
	 */
	private Image bufferImage ;

    private Graphics currentGraphics;
	private Graphics2D graphics2D;

	/**
	 * Constructor. Object is not focusable.
	 */
	public MazePanel() {
		super() ;
		this.setFocusable(false) ;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	public void update() {
		paint(getGraphics());
	}

	/**
	 * Draws the buffer image to the given graphics object.
	 * This method is called when this panel should redraw itself.
	 */
	@Override
	public void paint(Graphics g) {
		if (null == g) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation") ;
		}
		else {
			g.drawImage(bufferImage, 0, 0, null);
		}
	}

	public void initBufferImage() {
		bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		if (null == bufferImage)
		{
			System.out.println("Error: creation of buffered image failed, presumably container not displayable");
		}
	}
	/**
	 * Obtains a graphics object that can be used for drawing. 
	 * Multiple calls to the method will return the same graphics object 
	 * such that drawing operations can be performed in a piecemeal manner 
	 * and accumulate. To make the drawing visible on screen, one
	 * needs to trigger a call of the paint method, which happens 
	 * when calling the update method. 
	 * @return graphics object to draw on
	 */
	public Graphics getBufferGraphics() {
		if (null == bufferImage)
			initBufferImage() ;
		if (null == bufferImage)
			return null ;
		return bufferImage.getGraphics() ;
	}

    public void setGraphics() {
        this.currentGraphics = getBufferGraphics();
    }

    // Getter and Setter for 2DGraphics - stand in for FirstPersonDrawer class functions
    public void set2DGraphics() {
        this.graphics2D = (Graphics2D) getBufferGraphics();
    }

    public Graphics getGraphics2D() {
        return graphics2D;
    }

    /**
     * Sets color for the specified graphics object using an int
     * @param color an int of the combined RGB value for the color
     * @param is2D a boolean that tells whether to use the graphics2D object or not
     */
    public void setColor(int color, boolean is2D) {
        Color graphicsColor = new Color(color);

        if (is2D) {
            this.graphics2D.setColor(graphicsColor);
        } else {
            this.currentGraphics.setColor(graphicsColor);
        }
    }

    /**
     * Sets color for the specified graphics object
     * @param color a Constants.Colors enum that helps select which color will be used
     * @param is2D a boolean that tells whether to use the graphics2D object or not
     */
	public void setColor(Constants.Colors color, boolean is2D) {
        Color graphicsColor = null;

        switch (color) {
            case WHITE:
                graphicsColor = Color.white;
                break;
            case GRAY:
                graphicsColor = Color.gray;
                break;
            case RED:
                graphicsColor = Color.red;
                break;
            case YELLOW:
                graphicsColor = Color.yellow;
                break;
            case BLACK:
                graphicsColor = Color.black;
                break;
            case DARK_GRAY:
                graphicsColor = Color.darkGray;
                break;
            default:
                break;
        }

        if (is2D) {
            this.graphics2D.setColor(graphicsColor);
        } else {
            this.currentGraphics.setColor(graphicsColor);
        }
	}

    // Replacement functions to abstract away AWT from other classes
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.currentGraphics.drawLine(x1, y1, x2, y2);
    }

    public void fillOval(int x, int y, int width, int height) {
        this.currentGraphics.fillOval(x, y, width, height);
    }

    public void fillRect(int x, int y, int width, int height) {
        this.graphics2D.fillRect(x, y, width, height);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        this.graphics2D.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void configure2D() {
        // became necessary when lines of polygons that were not horizontal or vertical
        // looked ragged
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    /**
     * Returns the int RGB value for the specified array of RGB values
     * @param colors int list of RGB values for the specified color
     * @return int RGB value
     */
    public static int findRGB(int[] colors) {
        Color color = new Color(colors[0], colors[1], colors[2]);

        return color.getRGB();
    }
}
