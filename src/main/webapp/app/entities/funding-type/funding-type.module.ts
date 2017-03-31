import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	FundingTypeService,
	FundingTypePopupService,
	FundingTypeComponent,
	FundingTypeDetailComponent,
	FundingTypeDialogComponent,
	FundingTypePopupComponent,
	FundingTypeDeletePopupComponent,
	FundingTypeDeleteDialogComponent,
	fundingTypeRoute,
	fundingTypePopupRoute
} from "./";

let ENTITY_STATES = [
	...fundingTypeRoute,
	...fundingTypePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		FundingTypeComponent,
		FundingTypeDetailComponent,
		FundingTypeDialogComponent,
		FundingTypeDeleteDialogComponent,
		FundingTypePopupComponent,
		FundingTypeDeletePopupComponent,
	],
	entryComponents: [
		FundingTypeComponent,
		FundingTypeDialogComponent,
		FundingTypePopupComponent,
		FundingTypeDeleteDialogComponent,
		FundingTypeDeletePopupComponent,
	],
	providers: [
		FundingTypeService,
		FundingTypePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceFundingTypeModule {
}
