package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelEnderCrystal extends ModelBase {

    private ModelRenderer field_41057_g;

    private ModelRenderer field_41058_h;

    private ModelRenderer field_41059_i;

    public ModelEnderCrystal(float par1) {
        field_41058_h = new ModelRenderer(this, "glass");
        field_41058_h.setTextureOffset(0, 0).addBox(-4F, -4F, -4F, 8, 8, 8);
        field_41057_g = new ModelRenderer(this, "cube");
        field_41057_g.setTextureOffset(32, 0).addBox(-4F, -4F, -4F, 8, 8, 8);
        field_41059_i = new ModelRenderer(this, "base");
        field_41059_i.setTextureOffset(0, 16).addBox(-6F, 0.0F, -6F, 12, 4, 12);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GL11.glTranslatef(0.0F, -0.5F, 0.0F);
        field_41059_i.render(par7);
        GL11.glRotatef(par3, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.8F + par4, 0.0F);
        GL11.glRotatef(60F, 0.7071F, 0.0F, 0.7071F);
        field_41058_h.render(par7);
        float f = 0.875F;
        GL11.glScalef(f, f, f);
        GL11.glRotatef(60F, 0.7071F, 0.0F, 0.7071F);
        GL11.glRotatef(par3, 0.0F, 1.0F, 0.0F);
        field_41058_h.render(par7);
        GL11.glScalef(f, f, f);
        GL11.glRotatef(60F, 0.7071F, 0.0F, 0.7071F);
        GL11.glRotatef(par3, 0.0F, 1.0F, 0.0F);
        field_41057_g.render(par7);
        GL11.glPopMatrix();
    }

    /**
     * Sets the models various rotation angles.
     */
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6) {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6);
    }
}
