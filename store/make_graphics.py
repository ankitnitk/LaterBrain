"""Draws the Play Store icon (512x512) and feature graphic (1024x500).

Run from the repo root:  python store/make_graphics.py
Needs Pillow (pip install pillow). Uses Segoe UI fonts that come with Windows.
"""
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

OUT = Path(__file__).parent / "graphics"
SAGE = (63, 107, 92)
SAGE_DARK = (47, 84, 71)
PAPER = (250, 248, 244)
GOLD = (242, 196, 109)
SS = 4  # draw 4x bigger, then shrink, for smooth edges
FONTS = Path("C:/Windows/Fonts")


def bookmark(draw: ImageDraw.ImageDraw, x: float, y: float, w: float, h: float) -> None:
    """Bookmark ribbon with a gold dot. Same proportions as the launcher icon (40 x 48, notch 12)."""
    r = w * 0.1
    notch = h * 0.25
    draw.rounded_rectangle([x, y, x + w, y + h - notch], radius=r, fill=PAPER, corners=(True, True, False, False))
    draw.polygon([(x, y + h - notch - SS), (x + w, y + h - notch - SS), (x + w, y + h),
                  (x + w / 2, y + h - notch), (x, y + h)], fill=PAPER)
    cx, cy, dot = x + w / 2, y + h * (10 / 48), w * (6 / 40)
    draw.ellipse([cx - dot, cy - dot, cx + dot, cy + dot], fill=GOLD)


def icon() -> None:
    size = 512 * SS
    img = Image.new("RGB", (size, size), SAGE)
    d = ImageDraw.Draw(img)
    # The launcher icon shows the middle 72 of 108 units; the bookmark is 40 x 48 units of that.
    unit = size / 72
    bookmark(d, 16 * unit, 12 * unit, 40 * unit, 48 * unit)
    img.resize((512, 512), Image.LANCZOS).save(OUT / "icon-512.png")


def feature_graphic() -> None:
    w, h = 1024 * SS, 500 * SS
    img = Image.new("RGB", (w, h), SAGE)
    d = ImageDraw.Draw(img)
    # Soft darker circle behind the bookmark for depth.
    d.ellipse([-120 * SS, -60 * SS, 420 * SS, 560 * SS], fill=SAGE_DARK)
    bookmark(d, 110 * SS, 130 * SS, 200 * SS, 240 * SS)

    title = ImageFont.truetype(str(FONTS / "seguisb.ttf"), 84 * SS)
    tag = ImageFont.truetype(str(FONTS / "segoeui.ttf"), 34 * SS)
    tx = 400 * SS
    d.text((tx, 140 * SS), "Later Brain", font=title, fill=PAPER)
    d.text((tx, 262 * SS), "You saved it for a reason.", font=tag, fill=PAPER)
    d.text((tx, 308 * SS), "We'll remind you when", font=tag, fill=GOLD)
    d.text((tx, 354 * SS), "that reason arrives.", font=tag, fill=GOLD)
    img.resize((1024, 500), Image.LANCZOS).save(OUT / "feature-graphic-1024x500.png")


if __name__ == "__main__":
    OUT.mkdir(exist_ok=True)
    icon()
    feature_graphic()
    print("Saved to", OUT)
