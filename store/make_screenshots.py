"""Turns raw phone screenshots into 1080x1920 Play Store images with a caption on top.

Run from the repo root:  python store/make_screenshots.py
Edit SHOTS below to add screenshots or change captions.
"""
from pathlib import Path

from PIL import Image, ImageDraw, ImageFilter, ImageFont

HERE = Path(__file__).parent
OUT = HERE / "graphics"
FONTS = Path("C:/Windows/Fonts")
W, H = 1080, 1920
SAGE = (63, 107, 92)
PAPER = (250, 248, 244)
GOLD = (242, 196, 109)

# (source file, caption line 1 in white, caption line 2 in gold)
SHOTS = [
    ("SS2.jpeg", "Your calm drawer", "for things you mean to get back to"),
    ("SS1.jpeg", "Save in two seconds", "and choose when to be reminded"),
]


def frame(src: Path, line1: str, line2: str, out: Path) -> None:
    img = Image.new("RGB", (W, H), SAGE)
    d = ImageDraw.Draw(img)

    title = ImageFont.truetype(str(FONTS / "seguisb.ttf"), 76)
    sub = ImageFont.truetype(str(FONTS / "segoeui.ttf"), 44)
    d.text((W / 2, 150), line1, font=title, fill=PAPER, anchor="mm")
    d.text((W / 2, 235), line2, font=sub, fill=GOLD, anchor="mm")

    shot = Image.open(src).convert("RGB")
    top, bottom_margin = 320, 60
    target_h = H - top - bottom_margin
    scale = target_h / shot.height
    shot = shot.resize((round(shot.width * scale), target_h), Image.LANCZOS)
    x = (W - shot.width) // 2

    # Soft shadow, then the screenshot with rounded corners like a phone screen.
    radius = 44
    shadow = Image.new("L", (W, H), 0)
    ImageDraw.Draw(shadow).rounded_rectangle([x, top + 12, x + shot.width, top + 12 + shot.height], radius, fill=110)
    img.paste((20, 40, 33), mask=shadow.filter(ImageFilter.GaussianBlur(24)))
    mask = Image.new("L", shot.size, 0)
    ImageDraw.Draw(mask).rounded_rectangle([0, 0, shot.width - 1, shot.height - 1], radius, fill=255)
    img.paste(shot, (x, top), mask)

    img.save(out, optimize=True)


if __name__ == "__main__":
    OUT.mkdir(exist_ok=True)
    for i, (name, l1, l2) in enumerate(SHOTS, start=1):
        out = OUT / f"phone-{i}.png"
        frame(HERE / name, l1, l2, out)
        print("Saved", out)
