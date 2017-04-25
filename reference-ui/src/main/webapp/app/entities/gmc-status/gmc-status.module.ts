import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	GmcStatusService,
	GmcStatusPopupService,
	GmcStatusComponent,
	GmcStatusDetailComponent,
	GmcStatusDialogComponent,
	GmcStatusPopupComponent,
	GmcStatusDeletePopupComponent,
	GmcStatusDeleteDialogComponent,
	gmcStatusRoute,
	gmcStatusPopupRoute
} from "./";

let ENTITY_STATES = [
	...gmcStatusRoute,
	...gmcStatusPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		GmcStatusComponent,
		GmcStatusDetailComponent,
		GmcStatusDialogComponent,
		GmcStatusDeleteDialogComponent,
		GmcStatusPopupComponent,
		GmcStatusDeletePopupComponent,
	],
	entryComponents: [
		GmcStatusComponent,
		GmcStatusDialogComponent,
		GmcStatusPopupComponent,
		GmcStatusDeleteDialogComponent,
		GmcStatusDeletePopupComponent,
	],
	providers: [
		GmcStatusService,
		GmcStatusPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceGmcStatusModule {
}
