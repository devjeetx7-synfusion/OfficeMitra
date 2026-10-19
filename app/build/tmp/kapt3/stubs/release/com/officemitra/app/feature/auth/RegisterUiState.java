package com.officemitra.app.feature.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\'\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0093\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\t\u00a2\u0006\u0002\u0010\u0011J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\tH\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\tH\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0097\u0001\u0010-\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0010\u001a\u00020\tH\u00c6\u0001J\u0013\u0010.\u001a\u00020\t2\b\u0010/\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00100\u001a\u000201H\u00d6\u0001J\t\u00102\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0015R\u0011\u0010\u0010\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0013R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0015R\u0013\u0010\n\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0015R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0015R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0015R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0015\u00a8\u00063"}, d2 = {"Lcom/officemitra/app/feature/auth/RegisterUiState;", "", "fullName", "", "email", "mobileNumber", "password", "confirmPassword", "acceptTerms", "", "nameError", "emailError", "mobileError", "passwordError", "confirmPasswordError", "termsError", "isLoading", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "getAcceptTerms", "()Z", "getConfirmPassword", "()Ljava/lang/String;", "getConfirmPasswordError", "getEmail", "getEmailError", "getFullName", "getMobileError", "getMobileNumber", "getNameError", "getPassword", "getPasswordError", "getTermsError", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_release"})
public final class RegisterUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String fullName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String email = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String mobileNumber = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String password = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String confirmPassword = null;
    private final boolean acceptTerms = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String nameError = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String emailError = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String mobileError = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String passwordError = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String confirmPasswordError = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String termsError = null;
    private final boolean isLoading = false;

    public RegisterUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String confirmPassword, boolean acceptTerms, @org.jetbrains.annotations.Nullable()
    java.lang.String nameError, @org.jetbrains.annotations.Nullable()
    java.lang.String emailError, @org.jetbrains.annotations.Nullable()
    java.lang.String mobileError, @org.jetbrains.annotations.Nullable()
    java.lang.String passwordError, @org.jetbrains.annotations.Nullable()
    java.lang.String confirmPasswordError, @org.jetbrains.annotations.Nullable()
    java.lang.String termsError, boolean isLoading) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFullName() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmail() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMobileNumber() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPassword() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getConfirmPassword() {
        return null;
    }

    public final boolean getAcceptTerms() {
        return false;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getNameError() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEmailError() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMobileError() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPasswordError() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getConfirmPasswordError() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTermsError() {
        return null;
    }

    public final boolean isLoading() {
        return false;
    }

    public RegisterUiState() {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }

    public final boolean component13() {
        return false;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }

    public final boolean component6() {
        return false;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.officemitra.app.feature.auth.RegisterUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String confirmPassword, boolean acceptTerms, @org.jetbrains.annotations.Nullable()
    java.lang.String nameError, @org.jetbrains.annotations.Nullable()
    java.lang.String emailError, @org.jetbrains.annotations.Nullable()
    java.lang.String mobileError, @org.jetbrains.annotations.Nullable()
    java.lang.String passwordError, @org.jetbrains.annotations.Nullable()
    java.lang.String confirmPasswordError, @org.jetbrains.annotations.Nullable()
    java.lang.String termsError, boolean isLoading) {
        return null;
    }

    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }

    @java.lang.Override()
    public int hashCode() {
        return 0;
    }

    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}