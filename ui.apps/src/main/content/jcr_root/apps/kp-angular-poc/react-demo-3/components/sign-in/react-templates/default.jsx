function signInTemplate(React,FieldText,FieldPassword,Link,Button) {
  const result = this.props.result || {};
  const username = result.username || {};
  const password = result.password || {};
  const translate = this.context.translate;
  const validators = this.props.validators;

  const isProcessing = this.props.isProcessing;
  const isSubmitButtonDisabled = isProcessing || !this.state.isValid;

  // as string
  var authenticated = "" + this.props.isAuthenticated;
  return (
    <form onSubmit={(e) => this._onSubmit(e)} className="dxp-sign-in">
      <div>
        <h3 className="title">{translate('signIn.title')} - Demo</h3>
        <p>[reactContent]</p>
        <FieldText translate={translate}
                   label={translate('signIn.username.label')}
                   isValid={username.isValid}
                   validators={validators.username}
                   error={username.error && translate(username.error)}
                   autoComplete="off"
                   ref="username"
                   defaultValue={username.value}
                   useLabelFeedback={true}
                   disabled={isProcessing}
                   onChange={(e) => this._onUsernameChange(e)}/>

        <FieldPassword translate={translate}
                       label={translate('signIn.password.label')}
                       isValid={password.isValid}
                       error={translate(password.error)}
                       validators={validators.password}
                       autoComplete="off"
                       ref="password"
                       useLabelFeedback={true}
                       disabled={isProcessing}
                       onChange={(e) => this._onPasswordChange(e)}/>
      </div>
      <div>
        <div className="row button-wrapper">
          <div className="col-lg-6 col-md-12 col-sm-12 forgot-wrapper">
            <Link title={translate('signIn.forgotPassword')}>{translate('signIn.forgotPassword')}</Link>
          </div>
          <div className="col-lg-6 col-md-12 col-sm-12 submit-wrapper">
            <Button type="submit" className="primary submit" disabled={isSubmitButtonDisabled} processing={isProcessing}
                    onClick={(e) => this._onSubmitClick(e)}>
              {translate('signIn.submit')}
            </Button>
          </div>
        </div>
      </div>
      <div>
        <label htmlFor={'remember-me-checkbox'}>
          <input type="checkbox" id="remember-me-checkbox" defaultChecked={this.props.isRememberMeChecked} ref="rememberMe"
                 onChange={(e) => this._onRememberMeChange(e)}/>
          {translate('signIn.rememberMe')}
        </label>
      </div>
      <div>authenticated: {authenticated}</div>
    </form>
  );
}
