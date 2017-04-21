import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	LocalOfficeService,
	LocalOfficePopupService,
	LocalOfficeComponent,
	LocalOfficeDetailComponent,
	LocalOfficeDialogComponent,
	LocalOfficePopupComponent,
	LocalOfficeDeletePopupComponent,
	LocalOfficeDeleteDialogComponent,
	localOfficeRoute,
	localOfficePopupRoute,
	LocalOfficeResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...localOfficeRoute,
	...localOfficePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		LocalOfficeComponent,
		LocalOfficeDetailComponent,
		LocalOfficeDialogComponent,
		LocalOfficeDeleteDialogComponent,
		LocalOfficePopupComponent,
		LocalOfficeDeletePopupComponent,
	],
	entryComponents: [
		LocalOfficeComponent,
		LocalOfficeDialogComponent,
		LocalOfficePopupComponent,
		LocalOfficeDeleteDialogComponent,
		LocalOfficeDeletePopupComponent,
	],
	providers: [
		LocalOfficeService,
		LocalOfficePopupService,
		LocalOfficeResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceLocalOfficeModule {
}
