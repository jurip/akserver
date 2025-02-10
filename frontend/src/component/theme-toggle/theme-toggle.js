import {html, LitElement} from 'lit';
import {PolylitMixin} from '@vaadin/component-base/src/polylit-mixin.js';
import {defineCustomElement} from '@vaadin/component-base/src/define.js';
import {ElementMixin} from '@vaadin/component-base/src/element-mixin.js';
import {TooltipController} from "@vaadin/component-base/src/tooltip-controller";
import {css, ThemableMixin} from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import {buttonStyles} from '@vaadin/button/src/vaadin-button-base.js';
import {button as buttonLumoStyles} from '@vaadin/button/theme/lumo/vaadin-button-styles.js';
import {ButtonMixin} from '@vaadin/button/src/vaadin-button-mixin.js';

const themeToggleStyles = css`
    :host {
        background: transparent;
        color: var(--lumo-text-color);
        min-width: var(--lumo-button-size);
        padding-left: calc(var(--lumo-button-size) / 4);
        padding-right: calc(var(--lumo-button-size) / 4);
    }
`;

class ThemeToggle extends ButtonMixin(ElementMixin(ThemableMixin(PolylitMixin(LitElement)))) {

    static get is() {
        return 'theme-toggle';
    }

    static get styles() {
        return [buttonStyles, buttonLumoStyles, themeToggleStyles];
    }

    render() {
        return html`
              <vaadin-combo-box
                label="Country"
                item-label-path="name"
                item-value-path="id"
                .items="${this.items}"
              ></vaadin-combo-box>
            <div class="vaadin-button-container">
                <vaadin-icon icon="vaadin:adjust"></vaadin-icon>
            </div>

            <slot name="tooltip"></slot>
        `;
    }

    static get properties() {
        return {
            ariaLabel: {
                type: String,
                value: 'Theme toggle',
                reflectToAttribute: true,
            }
        };
    }

    constructor() {
        super();

        this._storageKey = "app-theme";
        
        //this.addEventListener('click', () => this.toggleTheme());
    }

    /** @protected */
    ready() {
        super.ready();

        this._tooltipController = new TooltipController(this);
        this.addController(this._tooltipController);
        this.applyStorageTheme();
    }

    applyStorageTheme() {
        let storageTheme = this.getStorageTheme();
        let currentTheme = this.getCurrentTheme();
        if (storageTheme && currentTheme !== storageTheme) {
            this.applyTheme(storageTheme);
        }
    }

    getStorageTheme() {
        return localStorage.getItem(this._storageKey);
    }

    getCurrentTheme() {
        return document.documentElement.getAttribute("theme");
    }

    toggleTheme() {
        const theme = this.getCurrentTheme();
        this.applyTheme(theme === "dark" ? "" : "dark");
    }

    applyTheme(theme) {
        document.documentElement.setAttribute("theme", theme);
        localStorage.setItem(this._storageKey, theme);

        const customEvent = new CustomEvent('theme-changed', {detail: {value: theme}});
        this.dispatchEvent(customEvent);
    }
}

defineCustomElement(ThemeToggle);

export {ThemeToggle};