import { afterNextRender, booleanAttribute, Directive, ElementRef, inject, input } from '@angular/core';

@Directive({
  selector: '[appAutofocus]',
  standalone: true
})
export class AutofocusDirective {
  
  private readonly _el = inject(ElementRef<HTMLElement>);

  // Permite <input appAutofocus> o <input [appAutofocus]="false">
  appAutofocus = input(true, { transform: booleanAttribute });

  constructor() {
    afterNextRender(() => {
      this.tryFocus();
    });
  }

  private tryFocus(): void {
    if (!this.appAutofocus()) return;

    const el = this._el.nativeElement;
    el.focus({ preventScroll: true });
  }

}