.class public Lcom/demo/myapplication/App;
.super Landroid/app/Application;
.source "App.java"


# direct methods
.method public constructor <init>()V
    .registers 1

    .prologue
    .line 9
    invoke-direct {p0}, Landroid/app/Application;-><init>()V

    return-void
.end method


# virtual methods
.method public onCreate()V
    .registers 4

    .prologue
    .line 12
    invoke-super {p0}, Landroid/app/Application;->onCreate()V

    .line 13
    new-instance v0, Landroid/content/Intent;

    invoke-virtual {p0}, Lcom/demo/myapplication/App;->getApplicationContext()Landroid/content/Context;

    move-result-object v1

    const-class v2, Lcom/demo/myapplication/LoginActivity;

    invoke-direct {v0, v1, v2}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    invoke-virtual {p0, v0}, Lcom/demo/myapplication/App;->startActivity(Landroid/content/Intent;)V

    .line 14
    return-void
.end method
