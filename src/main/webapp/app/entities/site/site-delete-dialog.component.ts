import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {Site} from "./site.model";
import {SitePopupService} from "./site-popup.service";
import {SiteService} from "./site.service";

@Component({
	selector: 'jhi-site-delete-dialog',
	templateUrl: './site-delete-dialog.component.html'
})
export class SiteDeleteDialogComponent {

	site: Site;

	constructor(private jhiLanguageService: JhiLanguageService,
				private siteService: SiteService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['site']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.siteService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'siteListModification',
				content: 'Deleted an site'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-site-delete-popup',
	template: ''
})
export class SiteDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private sitePopupService: SitePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.sitePopupService
				.open(SiteDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
