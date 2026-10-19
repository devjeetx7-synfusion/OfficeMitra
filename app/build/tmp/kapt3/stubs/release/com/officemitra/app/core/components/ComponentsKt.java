package com.officemitra.app.core.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000n\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\u001a\u00a1\u0001\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0015\b\u0002\u0010\f\u001a\u000f\u0012\u0004\u0012\u00020\u0001\u0018\u00010\r\u00a2\u0006\u0002\b\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u00102\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001aR\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u00032\b\b\u0002\u0010\u001c\u001a\u00020\u00032\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\u001f\u001a\u00020\u0010H\u0007\u001a6\u0010 \u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a.\u0010!\u001a\u00020\u00012\b\u0010\"\u001a\u0004\u0018\u00010\u00032\u0006\u0010#\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010$\u001a\u00020%H\u0007\u001aJ\u0010&\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\'\u001a\u00020\u00032\b\b\u0002\u0010(\u001a\u00020\u000b2\n\b\u0002\u0010)\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010*\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\r2\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a(\u0010+\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00032\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a<\u0010-\u001a\u00020\u00012\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00030/2\u0006\u00100\u001a\u00020\u00032\u0012\u00101\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001aB\u00102\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00032\b\b\u0002\u0010(\u001a\u00020\u000b2\b\b\u0002\u00103\u001a\u0002042\b\b\u0002\u00105\u001a\u0002042\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b6\u00107\u001a\u001c\u00108\u001a\u00020\u00012\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u0003H\u0007\u001a4\u00109\u001a\u00020\u00012\f\u0010:\u001a\b\u0012\u0004\u0012\u00020;0/2\b\u0010<\u001a\u0004\u0018\u00010\u00032\u0012\u0010=\u001a\u000e\u0012\u0004\u0012\u00020;\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001aJ\u0010>\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010?\u001a\u00020\u00102\u000e\b\u0002\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\u001e\b\u0002\u0010A\u001a\u0018\u0012\u0004\u0012\u00020B\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u000e\u00a2\u0006\u0002\bCH\u0007\u001a>\u0010D\u001a\u00020\u00012\u0006\u0010E\u001a\u00020\u00032\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010G\u001a\u00020\u00102\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u000bH\u0007\u001aN\u0010H\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0007\u001aH\u0010I\u001a\u00020\u00012\u0006\u0010E\u001a\u00020\u00032\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010G\u001a\u00020\u00102\b\b\u0002\u0010J\u001a\u00020\u00102\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u000bH\u0007\u001a:\u0010K\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u000b2\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010L\u001a\u00020%H\u0007\u001a>\u0010M\u001a\u00020\u00012\u0006\u0010E\u001a\u00020\u00032\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010G\u001a\u00020\u00102\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u000bH\u0007\u001a8\u0010N\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\n\b\u0002\u0010O\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010*\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\r2\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a\u001a\u0010P\u001a\u00020\u00012\u0006\u0010Q\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001aN\u0010R\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010S\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010T\u001a\u0002042\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bU\u0010V\u001a\u000e\u0010W\u001a\u00020\u0010*\u0004\u0018\u00010\u0003H\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006X"}, d2 = {"AppTextField", "", "value", "", "onValueChange", "Lkotlin/Function1;", "label", "placeholder", "modifier", "Landroidx/compose/ui/Modifier;", "leadingIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "trailingIcon", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "isError", "", "errorMessage", "singleLine", "readOnly", "keyboardOptions", "Landroidx/compose/foundation/text/KeyboardOptions;", "keyboardActions", "Landroidx/compose/foundation/text/KeyboardActions;", "ConfirmationDialog", "title", "message", "confirmText", "dismissText", "onConfirm", "onDismiss", "isDanger", "DatePickerField", "EmployeeAvatar", "imageUrl", "name", "size", "", "EmptyState", "description", "icon", "actionLabel", "onActionClick", "ErrorState", "onRetryClick", "FilterChipRow", "options", "", "selectedOption", "onOptionSelected", "InfoBanner", "backgroundColor", "Landroidx/compose/ui/graphics/Color;", "contentColor", "InfoBanner-OoHUuok", "(Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;JJLandroidx/compose/ui/Modifier;)V", "LoadingState", "OfficeMitraBottomBar", "items", "Lcom/officemitra/app/core/components/BottomNavItem;", "currentRoute", "onItemClick", "OfficeMitraTopBar", "showBackButton", "onBackClick", "actions", "Landroidx/compose/foundation/layout/RowScope;", "Lkotlin/ExtensionFunctionType;", "OutlineButton", "text", "onClick", "enabled", "PasswordTextField", "PrimaryButton", "isLoading", "QuickActionCard", "badgeCount", "SecondaryButton", "SectionHeader", "actionText", "StatusBadge", "status", "SummaryCard", "subtitle", "accentColor", "SummaryCard-jzV_Hc0", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;JLandroidx/compose/ui/Modifier;)V", "isNull_or_Empty", "app_release"})
public final class ComponentsKt {

    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void OfficeMitraTopBar(@org.jetbrains.annotations.NotNull()
    java.lang.String title, boolean showBackButton, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.RowScope, kotlin.Unit> actions) {
    }

    @androidx.compose.runtime.Composable()
    public static final void OfficeMitraBottomBar(@org.jetbrains.annotations.NotNull()
    java.util.List<com.officemitra.app.core.components.BottomNavItem> items, @org.jetbrains.annotations.Nullable()
    java.lang.String currentRoute, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.officemitra.app.core.components.BottomNavItem, kotlin.Unit> onItemClick) {
    }

    @androidx.compose.runtime.Composable()
    public static final void PrimaryButton(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean enabled, boolean isLoading, @org.jetbrains.annotations.Nullable()
    androidx.compose.ui.graphics.vector.ImageVector icon) {
    }

    @androidx.compose.runtime.Composable()
    public static final void SecondaryButton(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean enabled, @org.jetbrains.annotations.Nullable()
    androidx.compose.ui.graphics.vector.ImageVector icon) {
    }

    @androidx.compose.runtime.Composable()
    public static final void OutlineButton(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean enabled, @org.jetbrains.annotations.Nullable()
    androidx.compose.ui.graphics.vector.ImageVector icon) {
    }

    @androidx.compose.runtime.Composable()
    public static final void AppTextField(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String placeholder, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    androidx.compose.ui.graphics.vector.ImageVector leadingIcon, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> trailingIcon, boolean isError, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, boolean singleLine, boolean readOnly, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.text.KeyboardOptions keyboardOptions, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.text.KeyboardActions keyboardActions) {
    }

    private static final boolean isNull_or_Empty(java.lang.String $this$isNull_or_Empty) {
        return false;
    }

    @androidx.compose.runtime.Composable()
    public static final void PasswordTextField(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean isError, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
    }

    @androidx.compose.runtime.Composable()
    public static final void StatusBadge(@org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void EmployeeAvatar(@org.jetbrains.annotations.Nullable()
    java.lang.String imageUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, int size) {
    }

    @androidx.compose.runtime.Composable()
    public static final void QuickActionCard(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.graphics.vector.ImageVector icon, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, int badgeCount) {
    }

    @androidx.compose.runtime.Composable()
    public static final void SectionHeader(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String actionText, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onActionClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void EmptyState(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.graphics.vector.ImageVector icon, @org.jetbrains.annotations.Nullable()
    java.lang.String actionLabel, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onActionClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void ErrorState(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRetryClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void LoadingState(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }

    @androidx.compose.runtime.Composable()
    public static final void ConfirmationDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.lang.String confirmText, @org.jetbrains.annotations.NotNull()
    java.lang.String dismissText, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onConfirm, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, boolean isDanger) {
    }

    @androidx.compose.runtime.Composable()
    public static final void DatePickerField(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    public static final void FilterChipRow(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> options, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedOption, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOptionSelected, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}