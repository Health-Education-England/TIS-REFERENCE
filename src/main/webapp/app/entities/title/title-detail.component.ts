import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Title} from "./title.model";
import {TitleService} from "./title.service";

@Component({
	selector: 'jhi-title-detail',
	templateUrl: './title-detail.component.html'
})
export class TitleDetailComponent implements OnInit, OnDestroy {

	title: Title;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private titleService: TitleService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['title']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.titleService.find(id).subscribe(title => {
			this.title = title;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
