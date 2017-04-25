import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	EthnicOriginService,
	EthnicOriginPopupService,
	EthnicOriginComponent,
	EthnicOriginDetailComponent,
	EthnicOriginDialogComponent,
	EthnicOriginPopupComponent,
	EthnicOriginDeletePopupComponent,
	EthnicOriginDeleteDialogComponent,
	ethnicOriginRoute,
	ethnicOriginPopupRoute
} from "./";

let ENTITY_STATES = [
	...ethnicOriginRoute,
	...ethnicOriginPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		EthnicOriginComponent,
		EthnicOriginDetailComponent,
		EthnicOriginDialogComponent,
		EthnicOriginDeleteDialogComponent,
		EthnicOriginPopupComponent,
		EthnicOriginDeletePopupComponent,
	],
	entryComponents: [
		EthnicOriginComponent,
		EthnicOriginDialogComponent,
		EthnicOriginPopupComponent,
		EthnicOriginDeleteDialogComponent,
		EthnicOriginDeletePopupComponent,
	],
	providers: [
		EthnicOriginService,
		EthnicOriginPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceEthnicOriginModule {
}
