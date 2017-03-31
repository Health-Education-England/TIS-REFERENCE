import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	GdcStatusService,
	GdcStatusPopupService,
	GdcStatusComponent,
	GdcStatusDetailComponent,
	GdcStatusDialogComponent,
	GdcStatusPopupComponent,
	GdcStatusDeletePopupComponent,
	GdcStatusDeleteDialogComponent,
	gdcStatusRoute,
	gdcStatusPopupRoute
} from "./";

let ENTITY_STATES = [
	...gdcStatusRoute,
	...gdcStatusPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		GdcStatusComponent,
		GdcStatusDetailComponent,
		GdcStatusDialogComponent,
		GdcStatusDeleteDialogComponent,
		GdcStatusPopupComponent,
		GdcStatusDeletePopupComponent,
	],
	entryComponents: [
		GdcStatusComponent,
		GdcStatusDialogComponent,
		GdcStatusPopupComponent,
		GdcStatusDeleteDialogComponent,
		GdcStatusDeletePopupComponent,
	],
	providers: [
		GdcStatusService,
		GdcStatusPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceGdcStatusModule {
}
